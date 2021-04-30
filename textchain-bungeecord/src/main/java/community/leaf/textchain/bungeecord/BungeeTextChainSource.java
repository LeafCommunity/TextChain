/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.platforms.PlatformChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

@FunctionalInterface
public interface BungeeTextChainSource extends PlatformChainSource<BungeeAudiences, BungeeTextChain>
{
    @Override
    default ChainConstructor<BungeeTextChain> getChainConstructor()
    {
        return builder -> new BungeeTextChain(builder, adventure());
    }
}
