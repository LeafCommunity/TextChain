/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.adventure.LegacyColorAlias;
import community.leaf.textchain.platforms.adapters.ColorAdapter;
import org.bukkit.ChatColor;

class BukkitColorAdapter implements ColorAdapter<ChatColor>
{
    @Override
    public LegacyColorAlias format(ChatColor color)
    {
        return LegacyColorAlias.resolveByCharacter(color.getChar()).orElseThrow();
    }
}
