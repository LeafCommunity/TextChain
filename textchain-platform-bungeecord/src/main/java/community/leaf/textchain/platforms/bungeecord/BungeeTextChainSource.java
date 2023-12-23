/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bungeecord;

import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.platforms.PlatformTextChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

@FunctionalInterface
public interface BungeeTextChainSource extends PlatformTextChainSource<BungeeAudiences, BungeeTextChain>
{
    @Override
    default TextChainConstructor<BungeeTextChain> textChainConstructor()
    {
        return (builder, processor) -> new BungeeTextChainImpl(builder, processor, adventure());
    }
}
