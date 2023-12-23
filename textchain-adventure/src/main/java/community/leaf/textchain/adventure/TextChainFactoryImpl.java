/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;

final class TextChainFactoryImpl<T extends TextChain<T>> implements TextChainFactory<T>
{
    private final TextChainSource<T> source;
    
    private @NullOr TextProcessor processor;
    private @NullOr Style style;
    private TextComponent.@NullOr Builder builder;
    
    TextChainFactoryImpl(TextChainSource<T> source)
    {
        this.source = Objects.requireNonNull(source, "source");
    }
    
    @Override
    public TextChainFactory<T> processor(TextProcessor processor)
    {
        this.processor = processor;
        return this;
    }
    
    @Override
    public TextChainFactory<T> style(Style style)
    {
        this.style = style;
        return this;
    }
    
    @Override
    public TextChainFactory<T> wrap(TextComponent.Builder builder)
    {
        this.builder = builder;
        return this;
    }
    
    @Override
    public T chain()
    {
        LinearTextComponentBuilder builder =
            (this.builder != null)
                ? LinearTextComponentBuilder.wrap(this.builder)
                : LinearTextComponentBuilder.empty();
        
        TextProcessor processor = (this.processor != null) ? this.processor : TextProcessor.none();
        
        if (style != null) { builder.wrappedComponentBuilder().style(style); }
        
        return source.textChainConstructor().construct(builder, processor);
    }
}
