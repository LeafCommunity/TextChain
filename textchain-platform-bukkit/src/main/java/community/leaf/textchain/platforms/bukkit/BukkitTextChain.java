/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit;

import community.leaf.textchain.adventure.ChainedRecipientSender;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.platforms.AdventureSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;

public interface BukkitTextChain extends
    AdventureSource<BukkitAudiences>,
    ChainedRecipientSender<CommandSender, BukkitTextChain>,
    TextChain<BukkitTextChain>
{
    static TextChainConstructor<BukkitTextChain> using(BukkitAudiences audiences)
    {
        return (builder, processor) -> new BukkitTextChainImpl(builder, processor, audiences);
    }
    
    @Override
    default Audience recipientToAudience(CommandSender recipient)
    {
        return adventure().sender(recipient);
    }
}
