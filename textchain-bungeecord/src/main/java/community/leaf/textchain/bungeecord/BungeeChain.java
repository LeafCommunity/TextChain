/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.AbstractChain;
import community.leaf.textchain.adventure.ChainedRecipientSender;
import community.leaf.textchain.adventure.LinearTextComponentBuilderImpl;
import community.leaf.textchain.platforms.AdventureSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;

import java.util.Objects;

public abstract class BungeeChain<C extends BungeeChain<C>> extends AbstractChain<C>
    implements AdventureSource<BungeeAudiences>, ChainedRecipientSender<CommandSender, C>
{
    private final BungeeAudiences audiences;
    
    public BungeeChain(LinearTextComponentBuilderImpl builder, BungeeAudiences audiences)
    {
        super(builder);
        this.audiences = Objects.requireNonNull(audiences, "audiences");
    }
    
    @Override
    public final BungeeAudiences adventure() { return audiences; }
    
    @Override
    public final Audience recipientToAudience(CommandSender recipient)
    {
        return adventure().sender(recipient);
    }
}
