/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bungeecord;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainedRecipientSender;
import community.leaf.textchain.platforms.AdventureSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;

public interface BungeeTextChain extends
    AdventureSource<BungeeAudiences>,
    Chain<BungeeTextChain>,
    ChainedRecipientSender<CommandSender, BungeeTextChain>
{
    static ChainConstructor<BungeeTextChain> using(BungeeAudiences audiences)
    {
        return (builder, processor) -> new BungeeTextChainImpl(builder, processor, audiences);
    }
    
    @Override
    default Audience recipientToAudience(CommandSender recipient)
    {
        return adventure().sender(recipient);
    }
}
