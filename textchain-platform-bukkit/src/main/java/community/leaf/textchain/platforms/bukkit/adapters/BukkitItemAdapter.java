/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.adapters;

import community.leaf.textchain.platforms.ItemRarity;
import community.leaf.textchain.platforms.adapters.ItemMetaAdapter;
import community.leaf.textchain.platforms.adapters.ItemTypeAdapter;
import community.leaf.textchain.platforms.adapters.MetaAdapter;
import community.leaf.textchain.platforms.bukkit.internal.Reflect;
import community.leaf.textchain.platforms.bukkit.internal.nms.ItemReflection;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

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
        try { return BinaryTagHolder.of(ItemReflection.items().compoundTag(item)); }
        catch (Throwable throwable) { throw Reflect.safelyRethrow(throwable); }
    }
    
    @Override
    public ItemRarity rarity(ItemStack item)
    {
        if (!item.getType().isItem()) { return ItemRarity.COMMON; }
        try { return ItemReflection.items().rarity(item); }
        catch (Throwable throwable) { throw Reflect.safelyRethrow(throwable); }
    }
    
    @Override
    public String translationKey(ItemStack item)
    {
        try { return ItemReflection.items().translationKey(item); }
        catch (Throwable throwable) { throw Reflect.safelyRethrow(throwable); }
    }
    
    @Override
    public String clientName(ItemStack item)
    {
        return TranslationRegistry.INSTANCE.translate(translationKey(item));
    }
}
