/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure.serializers;

import net.kyori.adventure.text.Component;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.function.Function;

public interface ChainPlaceholderProcessor
{
    static ChainPlaceholderProcessor using(
        Function<String, @NullOr String> stringProcessor,
        Function<String, @NullOr Component> componentProcessor
    )
    {
        Objects.requireNonNull(stringProcessor, "stringProcessor");
        Objects.requireNonNull(componentProcessor, "componentProcessor");
        
        return new ChainPlaceholderProcessor()
        {
            @Override
            public @NullOr String processAsString(String input)
            {
                return stringProcessor.apply(input);
            }
    
            @Override
            public @NullOr Component processAsComponent(String input)
            {
                return componentProcessor.apply(input);
            }
        };
    }
    
    static ChainPlaceholderProcessor strings(Function<String, @NullOr String> stringProcessor)
    {
        Objects.requireNonNull(stringProcessor, "stringProcessor");
        
        return new ChainPlaceholderProcessor()
        {
            @Override
            public @NullOr String processAsString(String input)
            {
                return stringProcessor.apply(input);
            }
            
            @Override
            public @NullOr Component processAsComponent(String input)
            {
                @NullOr String text = stringProcessor.apply(input);
                return (text == null) ? null : Component.text(text);
            }
        };
    }
    
    @NullOr String processAsString(String input);
    
    @NullOr Component processAsComponent(String input);
}
