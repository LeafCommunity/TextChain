package community.leaf.textchain.adventure.serializers;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.LegacyColorAlias;
import community.leaf.textchain.adventure.LinearTextComponentBuilder;
import community.leaf.textchain.adventure.TextProcessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

final class ChainSerializerImpl implements ChainSerializer
{
    static final ChainPlaceholderProcessor NO_PLACEHOLDERS = ChainPlaceholderProcessor.strings(Function.identity());
    
    static final ChainSerializer NONE = new ChainSerializerImpl(TextProcessor.none(), NO_PLACEHOLDERS);
    
    static final ChainSerializer LEGACY = new ChainSerializerImpl(TextProcessor.legacyAmpersand(), NO_PLACEHOLDERS);
    
    private final TextProcessor processor;
    private final ChainPlaceholderProcessor placeholders;
    
    ChainSerializerImpl(TextProcessor processor, ChainPlaceholderProcessor placeholders)
    {
        this.processor = Objects.requireNonNull(processor, "processor");
        this.placeholders = Objects.requireNonNull(placeholders, "placeholders");
    }
    
    @Override
    public <C extends Chain<C>> C deserializeAsChain(ChainConstructor<C> constructor, List<Map<String, Object>> input)
    {
        return new Deserializer<>(constructor).deserialize(input);
    }
    
    @Override
    public List<Map<String, Object>> serialize(TextComponent component)
    {
        return new Serializer().serialize(component);
    }
    
    @Override
    public ChainSerializer.Builder toBuilder()
    {
        return new BuilderImpl(processor, placeholders);
    }
    
    static class BuilderImpl implements ChainSerializer.Builder
    {
        TextProcessor processor;
        ChainPlaceholderProcessor placeholders;
        
        BuilderImpl(TextProcessor processor, ChainPlaceholderProcessor placeholders)
        {
            this.processor = processor;
            this.placeholders = placeholders;
        }
        
        @Override
        public Builder processor(TextProcessor processor)
        {
            this.processor = Objects.requireNonNull(processor, "processor");
            return this;
        }
    
        @Override
        public Builder placeholders(ChainPlaceholderProcessor placeholders)
        {
            this.placeholders = Objects.requireNonNull(placeholders, "placeholders");
            return this;
        }
    
        @Override
        public ChainSerializer build()
        {
            return new ChainSerializerImpl(processor, placeholders);
        }
    }
    
    private static <K, V> Optional<V> query(Map<K, V> map, K key)
    {
        return Optional.ofNullable(map.get(key));
    }
    
    private static <K, V> Optional<String> string(Map<K, V> map, K key)
    {
        return query(map, key).filter(v -> v instanceof String).map(v -> (String) v);
    }
    
    @SuppressWarnings("unchecked")
    private static <K, V> List<Map<String, Object>> mapList(Map<K, V> map, K key)
    {
        return query(map, key)
            .filter(value -> value instanceof List)
            .map(value -> (List<Object>) value)
            .stream()
            .flatMap(List::stream)
            .filter(item -> item instanceof Map)
            .map(item -> (Map<String, Object>) item)
            .collect(Collectors.toList());
    }
    
    class Deserializer<C extends Chain<C>>
    {
        ChainConstructor<C> constructor;
        
        Deserializer(ChainConstructor<C> constructor)
        {
            this.constructor = Objects.requireNonNull(constructor, "constructor");
        }
        
        C deserialize(List<Map<String, Object>> input)
        {
            C chain  = constructor.construct(LinearTextComponentBuilder.empty(), processor);
            for (Map<String, Object> values : input) { deserialize(chain, values); }
            return chain;
        }
        
        private Consumer<Chain<C>> resolveStyleAction(String option)
        {
            @NullOr LegacyColorAlias alias = LegacyColorAlias.resolveByAlias(option).orElse(null);
            if (alias != null) { return chain -> chain.format(alias); }
            
            @NullOr TextColor color = TextColor.fromCSSHexString(option);
            if (color != null) { return chain -> chain.color(color); }
            
            return chain -> {};
        }
        
        private void deserialize(C chain, Map<String, Object> values)
        {
            @NullOr Component component = null;
            String text = string(values, "text").orElse("");
            List<Map<String, Object>> extra = mapList(values, "extra");
            
            if (!text.isEmpty())
            {
                component = placeholders.processAsComponent(text.replace("%nl%", "\n"));
            }
            
            if (component == null)
            {
                // Nothing left to do if this component has no text and no children.
                if (extra.isEmpty()) { return; }
                
                // Component has children but no text, so this component must by empty.
                component = Component.empty();
            }
            
            chain.then(component);
            
            string(values, "command").map(placeholders::processAsString).ifPresent(chain::command);
            string(values, "insertion").map(placeholders::processAsString).ifPresent(chain::insertion);
            string(values, "link").map(placeholders::processAsString).ifPresent(chain::link);
            string(values, "suggest").map(placeholders::processAsString).ifPresent(chain::suggest);
            
            string(values, "tooltip")
                .map(tooltip -> tooltip.replace("%nl%", "\n"))
                .map(placeholders::processAsComponent)
                .ifPresent(chain::tooltip);
            
            string(values, "style").ifPresent(style ->
                Arrays.stream(style.split(" "))
                    .map(this::resolveStyleAction)
                    .forEach(chain::apply)
            );
            
            if (!extra.isEmpty())
            {
                chain.extra(extraChain -> {
                    for (Map<String, Object> extraValues : extra) { deserialize(extraChain, extraValues); }
                });
            }
        }
    }
    
    class Serializer
    {
        List<Map<String, Object>> serialize(Component component)
        {
            List<Map<String, Object>> serialized = new ArrayList<>();
            
            
            
            return serialized;
        }
    }
}
