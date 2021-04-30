/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.platforms.adapters.ItemTypeAdapter;
import net.kyori.adventure.key.Key;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;

import static community.leaf.textchain.bukkit.internal.nms.ItemReflection.*;

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
        try
        {
            if (type.isBlock()) { return getNmsBlockName(getNmsBlockByMaterial(type)); }
            else { return getNmsItemName(getNmsItemByMaterial(type)); }
        }
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
        
        try
        {
            Object nmsItem = getNmsItemByMaterial(type);
            Object nmsRarity = getItemRarityOfNmsItem(nmsItem);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
}
