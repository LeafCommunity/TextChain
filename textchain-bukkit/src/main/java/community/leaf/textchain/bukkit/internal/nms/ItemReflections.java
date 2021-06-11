/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.internal.nms;

import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.bukkit.internal.BukkitVersion;
import community.leaf.textchain.bukkit.internal.ServerReflection;
import community.leaf.textchain.bukkit.internal.ThrowsOr;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

final class ItemReflections
{
    private ItemReflections() { throw new UnsupportedOperationException(); }
    
    static final ItemReflection ITEMS;
    
    static
    {
        ITEMS = (BukkitVersion.getServerVersion().isAtLeast(1, 17)) ? null : new Impl_v1_16();
    }
    
    private static class Impl_v1_16 implements ItemReflection
    {
        // These classes should exist as if importing them directly.
        private final Class<?> CRAFT_ITEM_STACK =
            ServerReflection.requireCraftClass("inventory.CraftItemStack");
        
        private final Class<?> CRAFT_MAGIC_NUMBERS =
            ServerReflection.requireCraftClass("util.CraftMagicNumbers");
        
        private final Class<?> NMS_ENUM_ITEM_RARITY =
            ServerReflection.requireNmsClass("EnumItemRarity");
        
        private final Class<?> NMS_BLOCK =
            ServerReflection.requireNmsClass("Block");
        
        private final Class<?> NMS_ITEM =
            ServerReflection.requireNmsClass("Item");
        
        private final Class<?> NMS_ITEM_STACK =
            ServerReflection.requireNmsClass("ItemStack");
        
        private final Class<?> NMS_NBT_TAG_COMPOUND =
            ServerReflection.requireNmsClass("NBTTagCompound");
        
        // Methods and fields that may or may not exist, but they most likely do in fact exist.
        // But let's not throw anything until called (like standard java behavior).
        
        // Bukkit ItemStack -> NMS ItemStack
        private final ThrowsOr<MethodHandle> AS_NMS_COPY =
            ServerReflection.requireStaticMethod(CRAFT_ITEM_STACK, "asNMSCopy", NMS_ITEM_STACK, ItemStack.class);
        
        private Object asNmsCopy(ItemStack itemStack) throws Throwable
        {
            return AS_NMS_COPY.getOrThrow().invoke(itemStack);
        }
        
        // NMS ItemStack -> NMS NbtCompoundTag
        private final ThrowsOr<MethodHandle> GET_OR_CREATE_TAG =
            ServerReflection.requireMethod(NMS_ITEM_STACK, "getOrCreateTag", NMS_NBT_TAG_COMPOUND);
        
        private Object getOrCreateTag(Object nmsItemStack) throws Throwable
        {
            return GET_OR_CREATE_TAG.getOrThrow().invoke(nmsItemStack);
        }
        
        // Bukkit Material -> NMS Block
        private final ThrowsOr<MethodHandle> GET_BLOCK_BY_MATERIAL =
            ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getBlock", NMS_BLOCK, Material.class);
        
        private Object getNmsBlockByMaterial(Material material) throws Throwable
        {
            return GET_BLOCK_BY_MATERIAL.getOrThrow().invoke(material);
        }
        
        // NMS Block -> String (name: translation key)
        private final ThrowsOr<MethodHandle> GET_BLOCK_NAME =
            ServerReflection.requireMethod(NMS_BLOCK, "i", String.class);
        
        private String getNmsBlockName(Object nmsBlock) throws Throwable
        {
            return String.valueOf(GET_BLOCK_NAME.getOrThrow().invoke(nmsBlock));
        }
        
        // Bukkit Item -> NMS Item
        private final ThrowsOr<MethodHandle> GET_ITEM_BY_MATERIAL =
            ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getItem", NMS_ITEM, Material.class);
        
        private Object getNmsItemByMaterial(Material material) throws Throwable
        {
            return GET_ITEM_BY_MATERIAL.getOrThrow().invoke(material);
        }
        
        // NMS Item -> String (name: translation key)
        private final ThrowsOr<MethodHandle> GET_ITEM_NAME =
            ServerReflection.requireMethod(NMS_ITEM, "getName", String.class);
        
        private String getNmsItemName(Object nmsItem) throws Throwable
        {
            return String.valueOf(GET_ITEM_NAME.getOrThrow().invoke(nmsItem));
        }
        
        // NMS ItemStack -> NMS EnumItemRarity
        private final ThrowsOr<MethodHandle> GET_ITEM_RARITY_BY_ITEM_STACK =
            ServerReflection.requireMethod(NMS_ITEM, "i", NMS_ENUM_ITEM_RARITY, NMS_ITEM_STACK);
        
        private Object getItemRarityOfNmsItemStack(Object nmsItem, Object nmsItemStack) throws Throwable
        {
            return GET_ITEM_RARITY_BY_ITEM_STACK.getOrThrow().invoke(nmsItem, nmsItemStack);
        }
        
        // NMS Item -> NMS EnumItemRarity
        private final ThrowsOr<Field> NMS_ITEM_RARITY_FIELD =
            ServerReflection.requireField(NMS_ITEM, "a", NMS_ENUM_ITEM_RARITY);
        
        private Object getItemRarityOfNmsItem(Object nmsItem) throws Throwable
        {
            return NMS_ITEM_RARITY_FIELD.getOrThrow().get(nmsItem);
        }
    
        @Override
        public String translationKey(Material type) throws Throwable
        {
            if (type.isBlock()) { return getNmsBlockName(getNmsBlockByMaterial(type)); }
            else { return getNmsItemName(getNmsItemByMaterial(type)); }
        }
        
        @Override
        public String toCompoundTag(ItemStack item) throws Throwable
        {
            return String.valueOf(getOrCreateTag(asNmsCopy(item)));
        }
    
        @Override
        public ItemRarity rarity(Material type) throws Throwable
        {
            Object nmsItem = getNmsItemByMaterial(type);
            Object nmsRarity = getItemRarityOfNmsItem(nmsItem);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
    
        @Override
        public ItemRarity rarity(ItemStack item) throws Throwable
        {
            Object nmsItem = getNmsItemByMaterial(item.getType());
            Object nmsItemStack = asNmsCopy(item);
            Object nmsRarity = getItemRarityOfNmsItemStack(nmsItem, nmsItemStack);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
    }
}
