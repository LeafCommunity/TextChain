/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bungeecord.adapters;

import community.leaf.textchain.adventure.LegacyColorCodeAlias;
import community.leaf.textchain.platforms.adapters.ColorAdapter;
import net.md_5.bungee.api.ChatColor;

class BungeeColorAdapter implements ColorAdapter<ChatColor>
{
    @Override
    public LegacyColorCodeAlias format(ChatColor color)
    {
        return LegacyColorCodeAlias.resolveByAlias(color.getName()).orElseThrow();
    }
}
