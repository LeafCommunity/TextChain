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
        ITEMS = (BukkitVersion.server().isAtLeast(1, 17)) ? new Impl_v1_17() : new Impl_v1_16();
    }
    
    /**
     * 1.x - 1.16
     */
    private static class Impl_v1_16 implements ItemReflection
    {
        // These classes should exist as if importing them directly.
        private static final Class<?> CRAFT_ITEM_STACK =
            ServerReflection.requireCraftClass("inventory.CraftItemStack");
        
        private static final Class<?> CRAFT_MAGIC_NUMBERS =
            ServerReflection.requireCraftClass("util.CraftMagicNumbers");
        
        private static final Class<?> NMS_ENUM_ITEM_RARITY =
            ServerReflection.requireNmsClass("EnumItemRarity");
        
        private static final Class<?> NMS_BLOCK =
            ServerReflection.requireNmsClass("Block");
        
        private static final Class<?> NMS_ITEM =
            ServerReflection.requireNmsClass("Item");
        
        private static final Class<?> NMS_ITEM_STACK =
            ServerReflection.requireNmsClass("ItemStack");
        
        private static final Class<?> NMS_NBT_TAG_COMPOUND =
            ServerReflection.requireNmsClass("NBTTagCompound");
        
        // Methods and fields that may or may not exist, but they most likely do in fact exist.
        // But let's not throw anything until called (like standard java behavior).
        
        // Bukkit ItemStack -> NMS ItemStack
        private static final ThrowsOr<MethodHandle> nmsItemStackByBukkitCopy =
            ServerReflection.requireStaticMethod(CRAFT_ITEM_STACK, "asNMSCopy", NMS_ITEM_STACK, ItemStack.class);
        
        // NMS ItemStack -> NMS NbtCompoundTag
        private static final ThrowsOr<MethodHandle> nmsNbtTagCompoundByNmsItemStack =
            ServerReflection.requireMethod(NMS_ITEM_STACK, "getOrCreateTag", NMS_NBT_TAG_COMPOUND);
        
        // Bukkit Material -> NMS Block
        private static final ThrowsOr<MethodHandle> nmsBlockByMaterial =
            ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getBlock", NMS_BLOCK, Material.class);
        
        // NMS Block -> String (name: translation key)
        private static final ThrowsOr<MethodHandle> nameByNmsBlock =
            ServerReflection.requireMethod(NMS_BLOCK, "i", String.class);
        
        // Bukkit Material -> NMS Item
        private static final ThrowsOr<MethodHandle> nmsItemByMaterial =
            ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getItem", NMS_ITEM, Material.class);
        
        // NMS Item -> String (name: translation key)
        private static final ThrowsOr<MethodHandle> nameByNmsItem =
            ServerReflection.requireMethod(NMS_ITEM, "getName", String.class);
        
        // NMS Item -> NMS ItemStack -> NMS EnumItemRarity
        private static final ThrowsOr<MethodHandle> nmsRarityByNmsItemStack =
            ServerReflection.requireMethod(NMS_ITEM, "i", NMS_ENUM_ITEM_RARITY, NMS_ITEM_STACK);
        
        // NMS Item -> NMS EnumItemRarity
        private static final ThrowsOr<Field> nmsItemRarityByNmsItem =
            ServerReflection.requireField(NMS_ITEM, "a", NMS_ENUM_ITEM_RARITY);
    
        @Override
        public String compoundTag(ItemStack item) throws Throwable
        {
            Object nmsItemStack = nmsItemStackByBukkitCopy.getOrThrow().invoke(item);
            Object nmsNbtTagCompound = nmsNbtTagCompoundByNmsItemStack.getOrThrow().invoke(nmsItemStack);
            return String.valueOf(nmsNbtTagCompound);
        }
        
        @Override
        public String translationKey(Material type) throws Throwable
        {
            if (type.isBlock())
            {
                Object nmsBlock = nmsBlockByMaterial.getOrThrow().invoke(type);
                return String.valueOf(nameByNmsBlock.getOrThrow().invoke(nmsBlock));
            }
            else
            {
                Object nmsItem = nmsItemByMaterial.getOrThrow().invoke(type);
                return String.valueOf(nameByNmsItem.getOrThrow().invoke(nmsItem));
            }
        }
    
        @Override
        public ItemRarity rarity(Material type) throws Throwable
        {
            Object nmsItem = nmsItemByMaterial.getOrThrow().invoke(type);
            Object nmsRarity = nmsItemRarityByNmsItem.getOrThrow().get(nmsItem);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
    
        @Override
        public ItemRarity rarity(ItemStack item) throws Throwable
        {
            Object nmsItem = nmsItemByMaterial.getOrThrow().invoke(item.getType());
            Object nmsItemStack = nmsItemStackByBukkitCopy.getOrThrow().invoke(item);
            Object nmsRarity = nmsRarityByNmsItemStack.getOrThrow().invoke(nmsItem, nmsItemStack);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
    }
    
    /**
     * 1.17+
     */
    private static class Impl_v1_17 implements ItemReflection
    {
        private static final Class<?> CRAFT_ITEM_STACK =
            ServerReflection.requireCraftClass("inventory.CraftItemStack");
        
        private static final Class<?> CRAFT_MAGIC_NUMBERS =
            ServerReflection.requireCraftClass("util.CraftMagicNumbers");
        
        private static final Class<?> NMS_ENUM_ITEM_RARITY =
            ServerReflection.requireNmsClass("world.item.EnumItemRarity");
        
        private static final Class<?> NMS_BLOCK =
            ServerReflection.requireNmsClass("world.level.block.Block");
        
        private static final Class<?> NMS_ITEM =
            ServerReflection.requireNmsClass("world.item.Item");
        
        private static final Class<?> NMS_ITEM_STACK =
            ServerReflection.requireNmsClass("world.item.ItemStack");
        
        private static final Class<?> NMS_NBT_TAG_COMPOUND =
            ServerReflection.requireNmsClass("nbt.NBTTagCompound");
        
        // Bukkit ItemStack -> NMS ItemStack
        private static final ThrowsOr<MethodHandle> nmsItemStackByBukkitCopy =
            ServerReflection.requireStaticMethod(CRAFT_ITEM_STACK, "asNMSCopy", NMS_ITEM_STACK, ItemStack.class);
        
        // NMS ItemStack -> NMS NbtCompoundTag
        private static final ThrowsOr<MethodHandle> nmsNbtTagCompoundByNmsItemStack =
            ServerReflection.requireMethod(NMS_ITEM_STACK, "getOrCreateTag", NMS_NBT_TAG_COMPOUND);
        
        // Bukkit Material -> NMS Block
        private static final ThrowsOr<MethodHandle> nmsBlockByMaterial =
            ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getBlock", NMS_BLOCK, Material.class);
        
        // NMS Block -> String (name: translation key)
        private static final ThrowsOr<MethodHandle> nameByNmsBlock =
            ServerReflection.requireMethod(NMS_BLOCK, "h", String.class);
        
        // Bukkit Material -> NMS Item
        private static final ThrowsOr<MethodHandle> nmsItemByMaterial =
            ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getItem", NMS_ITEM, Material.class);
        
        // NMS Item -> String (name: translation key)
        private static final ThrowsOr<MethodHandle> nameByNmsItem =
            ServerReflection.requireMethod(NMS_ITEM, "getName", String.class);
        
        // NMS Item -> NMS ItemStack -> NMS EnumItemRarity
        private static final ThrowsOr<MethodHandle> nmsRarityByNmsItemStack =
            ServerReflection.requireMethod(NMS_ITEM, "n", NMS_ENUM_ITEM_RARITY, NMS_ITEM_STACK);
        
        // NMS Item -> NMS EnumItemRarity
        private static final ThrowsOr<Field> nmsItemRarityByNmsItem =
            ServerReflection.requireField(NMS_ITEM, "rarity", NMS_ENUM_ITEM_RARITY);
        
        @Override
        public String compoundTag(ItemStack item) throws Throwable
        {
            Object nmsItemStack = nmsItemStackByBukkitCopy.getOrThrow().invoke(item);
            Object nmsNbtTagCompound = nmsNbtTagCompoundByNmsItemStack.getOrThrow().invoke(nmsItemStack);
            return String.valueOf(nmsNbtTagCompound);
        }
        
        @Override
        public String translationKey(Material type) throws Throwable
        {
            if (type.isBlock())
            {
                Object nmsBlock = nmsBlockByMaterial.getOrThrow().invoke(type);
                return String.valueOf(nameByNmsBlock.getOrThrow().invoke(nmsBlock));
            }
            else
            {
                Object nmsItem = nmsItemByMaterial.getOrThrow().invoke(type);
                return String.valueOf(nameByNmsItem.getOrThrow().invoke(nmsItem));
            }
        }
    
        @Override
        public ItemRarity rarity(Material type) throws Throwable
        {
            Object nmsItem = nmsItemByMaterial.getOrThrow().invoke(type);
            Object nmsRarity = nmsItemRarityByNmsItem.getOrThrow().get(nmsItem);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
    
        @Override
        public ItemRarity rarity(ItemStack item) throws Throwable
        {
            Object nmsItem = nmsItemByMaterial.getOrThrow().invoke(item.getType());
            Object nmsItemStack = nmsItemStackByBukkitCopy.getOrThrow().invoke(item);
            Object nmsRarity = nmsRarityByNmsItemStack.getOrThrow().invoke(nmsItem, nmsItemStack);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
    }
}
