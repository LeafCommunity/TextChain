/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainedRecipientSender;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import community.leaf.textchain.platforms.AdventureSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public abstract class BukkitChain<C extends BukkitChain<C>> extends Chain<C>
    implements AdventureSource<BukkitAudiences>, ChainedRecipientSender<CommandSender, C>
{
    private final BukkitAudiences audiences;
    
    public BukkitChain(WrappedTextComponentBuilder builder, BukkitAudiences audiences)
    {
        super(builder);
        this.audiences = Objects.requireNonNull(audiences, "audiences");
    }
    
    @Override
    public final BukkitAudiences adventure() { return audiences; }
    
    @Override
    public final Audience recipientToAudience(CommandSender recipient)
    {
        return adventure().sender(recipient);
    }
}
