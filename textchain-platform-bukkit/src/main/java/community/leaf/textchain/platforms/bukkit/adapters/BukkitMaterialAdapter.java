/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.adapters;

import community.leaf.textchain.platforms.ItemRarity;
import community.leaf.textchain.platforms.adapters.ItemTypeAdapter;
import community.leaf.textchain.platforms.bukkit.internal.nms.ItemReflection;
import net.kyori.adventure.key.Key;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;

class BukkitMaterialAdapter implements ItemTypeAdapter<Material>
{
    private final BukkitKeyAdapter keys;
    
    public BukkitMaterialAdapter(BukkitKeyAdapter keys) { this.keys = keys; }
    
    @Override
    public Key key(Material type)
    {
        return keys.key(type.getKey());
    }
    
    @Override
    public String translationKey(Material type)
    {
        try { return ItemReflection.items().translationKey(type); }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    @Override
    public String clientName(Material type)
    {
        return TranslationRegistry.INSTANCE.translate(translationKey(type));
    }
    
    @Override
    public ItemRarity rarity(Material type)
    {
        if (!type.isItem()) { return ItemRarity.COMMON; }
        try { return ItemReflection.items().rarity(type); }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
}
