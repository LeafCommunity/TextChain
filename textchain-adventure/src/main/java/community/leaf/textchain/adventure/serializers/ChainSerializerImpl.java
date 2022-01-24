/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure.serializers;

import community.leaf.textchain.adventure.LegacyColorCodeAlias;
import community.leaf.textchain.adventure.LinearTextComponentBuilder;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.adventure.TextProcessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
    public <T extends TextChain<T>> T deserializeAsChain(TextChainConstructor<T> constructor, List<Map<String, Object>> input)
    {
        return new Deserializer<>(constructor).deserialize(input);
    }
    
    @Override
    public List<Map<String, Object>> serialize(Component component)
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
    
    class Deserializer<T extends TextChain<T>>
    {
        TextChainConstructor<T> constructor;
        
        Deserializer(TextChainConstructor<T> constructor)
        {
            this.constructor = Objects.requireNonNull(constructor, "constructor");
        }
        
        T deserialize(List<Map<String, Object>> input)
        {
            T chain  = constructor.construct(LinearTextComponentBuilder.empty(), processor);
            for (Map<String, Object> values : input) { deserialize(chain, values); }
            return chain;
        }
        
        private Consumer<TextChain<T>> resolveStyleAction(String option)
        {
            @NullOr LegacyColorCodeAlias alias = LegacyColorCodeAlias.resolveByAlias(option).orElse(null);
            if (alias != null) { return chain -> chain.format(alias); }
            
            @NullOr TextColor color = TextColor.fromCSSHexString(option.replaceFirst("(?i)^0x", "#"));
            if (color != null) { return chain -> chain.color(color); }
            
            return chain -> {};
        }
        
        private void deserialize(T chain, Map<String, Object> values)
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
    
    static class Serializer
    {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> serialize(Component component)
        {
            @NullOr Map<String, Object> serialized = serializeAsMap(component);
            if (serialized == null) { return new ArrayList<>(); }
            
            if (serialized.size() == 1 && serialized.containsKey("extra"))
            {
                return (List<Map<String, Object>>) serialized.get("extra");
            }
            
            List<Map<String, Object>> result = new ArrayList<>();
            result.add(serialized);
            return result;
        }
        
        @NullOr Map<String, Object> serializeAsMap(Component component)
        {
            String text = (component instanceof TextComponent) ? ((TextComponent) component).content() : "";
            if (text.isEmpty() && component.children().isEmpty()) { return null; }
            
            Map<String, Object> values = new LinkedHashMap<>();
            
            // Text
            if (!text.isEmpty()) { values.put("text", text.replace("\n", "%nl%")); }
            
            // Tooltip
            Optional.ofNullable(component.hoverEvent())
                .filter(hover -> hover.action() == HoverEvent.Action.SHOW_TEXT)
                .map(HoverEvent::value)
                .filter(value -> value instanceof Component)
                .map(value -> LegacyComponentSerializer.legacyAmpersand().serialize((Component) value))
                .ifPresent(tooltip -> values.put("tooltip", tooltip));
            
            // Clickables
            @NullOr ClickEvent click = component.clickEvent();
            
            if (click != null && !click.value().isEmpty())
            {
                String clickValue = click.value();
                ClickEvent.Action action = click.action();
                
                if (action == ClickEvent.Action.RUN_COMMAND) { values.put("command", clickValue); }
                else if (action == ClickEvent.Action.OPEN_URL) { values.put("link", clickValue); }
                else if (action == ClickEvent.Action.SUGGEST_COMMAND) { values.put("suggest", clickValue); }
            }
            
            // Insertion
            @NullOr String insertion = component.insertion();
            if (insertion != null && !insertion.isEmpty()) { values.put("insertion", insertion); }
            
            // Styles
            List<String> style = new ArrayList<>();
        
            @NullOr TextColor color = component.color();
            if (color != null)
            {
                NamedTextColor namedColorValue = NamedTextColor.nearestTo(color);
                @NullOr String namedColorName = NamedTextColor.NAMES.key(namedColorValue);
                
                if (namedColorValue.value() == color.value() && namedColorName != null)
                {
                    style.add(namedColorName.toLowerCase().replace("_", "-"));
                }
                else
                {
                    style.add(color.asHexString());
                }
            }
        
            if (component.hasDecoration(TextDecoration.BOLD)) { style.add("bold"); }
            if (component.hasDecoration(TextDecoration.ITALIC)) { style.add("italic"); }
            if (component.hasDecoration(TextDecoration.OBFUSCATED)) { style.add("obfuscated"); }
            if (component.hasDecoration(TextDecoration.STRIKETHROUGH)) { style.add("strikethrough"); }
            if (component.hasDecoration(TextDecoration.UNDERLINED)) { style.add("underlined"); }
        
            if (!style.isEmpty()) { values.put("style", String.join(" ", style)); }
            
            // Children
            if (!component.children().isEmpty())
            {
                List<Map<String, Object>> extra = new ArrayList<>();
                
                for (Component child : component.children())
                {
                    @NullOr Map<String, Object> childValues = serializeAsMap(child);
                    if (childValues != null) { extra.add(childValues); }
                }
                
                if (!extra.isEmpty()) { values.put("extra", extra); }
            }
            
            return values;
        }
    }
}
