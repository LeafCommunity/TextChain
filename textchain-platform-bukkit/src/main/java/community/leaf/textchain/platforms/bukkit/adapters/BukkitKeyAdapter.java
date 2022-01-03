/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.adapters;

import community.leaf.textchain.platforms.adapters.KeyAdapter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;

class BukkitKeyAdapter implements KeyAdapter<NamespacedKey, org.bukkit.Keyed>
{
    @SuppressWarnings("PatternValidation")
    @Override
    public Key key(NamespacedKey key)
    {
        return Key.key(key.getNamespace(), key.getKey());
    }
    
    @Override
    public Keyed keyed(org.bukkit.Keyed keyed)
    {
        Key converted = key(keyed.getKey());
        return () -> converted;
    }
}
