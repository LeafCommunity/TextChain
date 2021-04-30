/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bungeecord.adapters;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import community.leaf.textchain.platforms.adapters.ColorAdapter;
import net.md_5.bungee.api.ChatColor;

class BungeeColorAdapter implements ColorAdapter<ChatColor>
{
    @Override
    public LegacyColorCodeAliases format(ChatColor color)
    {
        return LegacyColorCodeAliases.resolveByAlias(color.getName()).orElseThrow();
    }
}
