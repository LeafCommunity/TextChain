/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.platforms.PlatformChainSource;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@FunctionalInterface
public interface BukkitTextChainSource extends PlatformChainSource<BukkitAudiences, BukkitTextChain>
{
    @Override
    default ChainConstructor<BukkitTextChain> getChainConstructor()
    {
        return (builder, processor) -> new BukkitTextChainImpl(builder, processor, adventure());
    }
}
