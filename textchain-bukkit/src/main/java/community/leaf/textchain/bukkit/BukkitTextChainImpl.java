/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.LinearTextComponentBuilder;
import community.leaf.textchain.adventure.TextProcessor;
import community.leaf.textchain.platforms.AbstractPlatformChain;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

final class BukkitTextChainImpl extends AbstractPlatformChain<BukkitAudiences, BukkitTextChain> implements BukkitTextChain
{
    BukkitTextChainImpl(LinearTextComponentBuilder builder, TextProcessor processor, BukkitAudiences audiences)
    {
        super(builder, processor, audiences);
    }
    
    @Override
    public ChainConstructor<BukkitTextChain> constructor()
    {
        return (builder, processor) -> new BukkitTextChainImpl(builder, processor, adventure());
    }
}
