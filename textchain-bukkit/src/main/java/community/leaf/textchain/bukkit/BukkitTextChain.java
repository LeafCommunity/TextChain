/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.TextProcessor;
import community.leaf.textchain.adventure.LinearTextComponentBuilderImpl;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.TextComponent;

public final class BukkitTextChain extends BukkitChain<BukkitTextChain>
{
    public BukkitTextChain(LinearTextComponentBuilderImpl builder, BukkitAudiences audiences)
    {
        super(builder, audiences);
    }
    
    @Override
    public ChainConstructor<BukkitTextChain> constructor()
    {
        return builder -> new BukkitTextChain(builder, adventure());
    }
    
    @Override
    public TextComponent processText(String text)
    {
        return TextProcessor.none(text);
    }
}
