package community.leaf.textchain.adventure.serializers;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.adventure.TextProcessor;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;

import java.util.List;
import java.util.Map;

public interface ChainSerializer extends
    Buildable<ChainSerializer, ChainSerializer.Builder>,
    ComponentSerializer<TextComponent, TextComponent, List<Map<String, Object>>>
{
    static ChainSerializer none()
    {
        return ChainSerializerImpl.NONE;
    }
    
    static ChainSerializer legacyAmpersand()
    {
        return ChainSerializerImpl.LEGACY;
    }
    
    static ChainSerializer.Builder builder()
    {
        return ChainSerializerImpl.NONE.toBuilder();
    }
    
    <C extends Chain<C>> C deserializeAsChain(ChainConstructor<C> constructor, List<Map<String, Object>> input);
    
    default <C extends Chain<C>> C deserializeAsChain(ChainSource<C> source, List<Map<String, Object>> input)
    {
        return deserializeAsChain(source.getChainConstructor(), input);
    }
    
    default TextChain deserializeAsTextChain(List<Map<String, Object>> input)
    {
        return deserializeAsChain((ChainConstructor<TextChain>) TextChain::chain, input);
    }
    
    @Override
    default TextComponent deserialize(List<Map<String, Object>> input)
    {
        return deserializeAsTextChain(input).asComponent();
    }
    
    default <C extends Chain<C>> List<Map<String, Object>> serialize(C chain)
    {
        return serialize(chain.asComponent());
    }
    
    interface Builder extends Buildable.Builder<ChainSerializer>
    {
        Builder processor(TextProcessor processor);
        
        Builder placeholders(ChainPlaceholderProcessor placeholders);
    }
}
