/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.platforms.adapters.ItemMetaAdapter;
import community.leaf.textchain.platforms.adapters.ItemTypeAdapter;
import community.leaf.textchain.platforms.adapters.MetaAdapter;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

import static community.leaf.textchain.bukkit.internal.nms.ItemReflection.*;

class BukkitItemAdapter implements ItemMetaAdapter<Material, ItemStack, ItemMeta>
{
    private final BukkitMaterialAdapter materials;
    private final BukkitMetaAdapter meta;
    
    public BukkitItemAdapter(BukkitMaterialAdapter materials, BukkitMetaAdapter meta)
    {
        this.materials = materials;
        this.meta = meta;
    }
    
    @Override
    public ItemTypeAdapter<Material> types()
    {
        return materials;
    }
    
    @Override
    public Material type(ItemStack item)
    {
        return item.getType();
    }
    
    @Override
    public MetaAdapter<ItemMeta> meta()
    {
        return meta;
    }
    
    @Override
    public @NullOr ItemMeta meta(ItemStack item)
    {
        return item.getItemMeta();
    }
    
    @Override
    public void meta(ItemStack item, @NullOr ItemMeta meta)
    {
        item.setItemMeta(meta);
    }
    
    @Override
    public int amount(ItemStack item)
    {
        return item.getAmount();
    }
    
    @Override
    public BinaryTagHolder nbt(ItemStack item)
    {
        try { return BinaryTagHolder.of(String.valueOf(getOrCreateTag(asNmsCopy(item)))); }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    @Override
    public ItemRarity rarity(ItemStack item)
    {
        Material type = item.getType();
        if (!type.isItem()) { return ItemRarity.COMMON; }
        
        try
        {
            Object nmsItem = getNmsItemByMaterial(type);
            Object nmsItemStack = asNmsCopy(item);
            Object nmsRarity = getItemRarityOfNmsItemStack(nmsItem, nmsItemStack);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
}
