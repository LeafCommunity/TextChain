/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bungeecord.adapters;

import community.leaf.textchain.platforms.adapters.ColorAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

public class BungeeToAdventure
{
    private BungeeToAdventure() { throw new UnsupportedOperationException(); }
    
    private static final BungeeColorAdapter colors = new BungeeColorAdapter();
    
    public static ColorAdapter<ChatColor> colors() { return colors; }
    
    public static Component component(BaseComponent[] baseComponents)
    {
        return BungeeComponentSerializer.get().deserialize(baseComponents);
    }
}
