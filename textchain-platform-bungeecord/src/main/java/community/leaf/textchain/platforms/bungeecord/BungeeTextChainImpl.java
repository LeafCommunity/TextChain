/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bungeecord;

import community.leaf.textchain.adventure.LinearTextComponentBuilder;
import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.adventure.TextProcessor;
import community.leaf.textchain.platforms.AbstractPlatformTextChain;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

final class BungeeTextChainImpl extends AbstractPlatformTextChain<BungeeAudiences, BungeeTextChain> implements BungeeTextChain
{
    public BungeeTextChainImpl(LinearTextComponentBuilder builder, TextProcessor processor, BungeeAudiences audiences)
    {
        super(builder, processor, audiences);
    }
    
    @Override
    public TextChainConstructor<BungeeTextChain> constructor()
    {
        return (builder, processor) -> new BungeeTextChainImpl(builder, processor, adventure());
    }
}
