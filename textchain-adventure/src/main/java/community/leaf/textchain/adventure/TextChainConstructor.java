/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.TextComponent;

/**
 * Standard text chain constructor.
 *
 * @param <T>   chain type
 */
@FunctionalInterface
public interface TextChainConstructor<T extends TextChain<T>>
{
    T construct(LinearTextComponentBuilder builder, TextProcessor processor);
    
    default T construct(TextComponent.Builder builder, TextProcessor processor)
    {
        return construct(LinearTextComponentBuilder.wrap(builder), processor);
    }
}
