/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit;

import community.leaf.textchain.platforms.bukkit.internal.BukkitVersion;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LegacyBukkitComponentSerializer
{
    private LegacyBukkitComponentSerializer() { throw new UnsupportedOperationException(); }
    
    private static final LegacyComponentSerializer LEGACY_HEX_SECTION;
    
    private static final LegacyComponentSerializer LEGACY_HEX_AMPERSAND;
    
    static
    {
        if (BukkitVersion.server().isAtLeast(1, 16))
        {
            LEGACY_HEX_SECTION =
                LegacyComponentSerializer.builder()
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();
    
            LEGACY_HEX_AMPERSAND =
                LegacyComponentSerializer.builder()
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .character(LegacyComponentSerializer.AMPERSAND_CHAR)
                    .build();
        }
        else
        {
            LEGACY_HEX_SECTION = LegacyComponentSerializer.legacySection();
            LEGACY_HEX_AMPERSAND = LegacyComponentSerializer.legacyAmpersand();
        }
    }
    
    public static LegacyComponentSerializer legacyHexSection() { return LEGACY_HEX_SECTION; }
    
    public static LegacyComponentSerializer legacyHexAmpersand() { return LEGACY_HEX_AMPERSAND; }
}
