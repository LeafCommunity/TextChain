/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure.serializers;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.adventure.TextChainSource;
import community.leaf.textchain.adventure.TextProcessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public interface ChainSerializer extends
    Buildable<ChainSerializer, ChainSerializer.Builder>,
    ComponentSerializer<Component, TextComponent, List<Map<String, Object>>>
{
    static ChainSerializer unprocessed()
    {
        return ChainSerializerImpl.NONE;
    }
    
    static ChainSerializer legacyAmpersand()
    {
        return ChainSerializerImpl.LEGACY;
    }
    
    static ChainSerializer.Builder builder()
    {
        return unprocessed().toBuilder();
    }
    
    <T extends TextChain<T>> T deserializeAsChain(TextChainConstructor<T> constructor, List<Map<String, Object>> input);
    
    default <T extends TextChain<T>> T deserializeAsChain(TextChainSource<T> source, List<Map<String, Object>> input)
    {
        return deserializeAsChain(source.textChainConstructor(), input);
    }
    
    default TextChain<?> deserializeAsTextChain(List<Map<String, Object>> input)
    {
        return deserializeAsChain(TextChain.source(), input);
    }
    
    @Override
    default TextComponent deserialize(List<Map<String, Object>> input)
    {
        return deserializeAsTextChain(input).asComponent();
    }
    
    default <T extends TextChain<T>> List<Map<String, Object>> serialize(T chain)
    {
        return serialize(chain.asComponent());
    }
    
    interface Builder extends Buildable.Builder<ChainSerializer>
    {
        Builder processor(@NullOr TextProcessor processor);
        
        Builder placeholders(@NullOr UnaryOperator<String> placeholders);
    }
}
