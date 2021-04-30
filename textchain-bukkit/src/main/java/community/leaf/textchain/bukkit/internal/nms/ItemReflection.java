/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.internal.nms;

import community.leaf.textchain.bukkit.internal.ServerReflection;
import community.leaf.textchain.bukkit.internal.ThrowsOr;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

public class ItemReflection
{
    private ItemReflection() { throw new UnsupportedOperationException(); }
    
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
    private static final ThrowsOr<MethodHandle> AS_NMS_COPY =
        ServerReflection.requireStaticMethod(CRAFT_ITEM_STACK, "asNMSCopy", NMS_ITEM_STACK, ItemStack.class);
    
    public static Object asNmsCopy(ItemStack itemStack) throws Throwable
    {
        return AS_NMS_COPY.getOrThrow().invoke(itemStack);
    }
    
    // NMS ItemStack -> NMS NbtCompoundTag
    private static final ThrowsOr<MethodHandle> GET_OR_CREATE_TAG =
        ServerReflection.requireMethod(NMS_ITEM_STACK, "getOrCreateTag", NMS_NBT_TAG_COMPOUND);
    
    public static Object getOrCreateTag(Object nmsItemStack) throws Throwable
    {
        return GET_OR_CREATE_TAG.getOrThrow().invoke(nmsItemStack);
    }
    
    // Bukkit Material -> NMS Block
    private static final ThrowsOr<MethodHandle> GET_BLOCK_BY_MATERIAL =
        ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getBlock", NMS_BLOCK, Material.class);
    
    public static Object getNmsBlockByMaterial(Material material) throws Throwable
    {
        return GET_BLOCK_BY_MATERIAL.getOrThrow().invoke(material);
    }
    
    // NMS Block -> String (name: translation key)
    private static final ThrowsOr<MethodHandle> GET_BLOCK_NAME =
        ServerReflection.requireMethod(NMS_BLOCK, "i", String.class);
    
    public static String getNmsBlockName(Object nmsBlock) throws Throwable
    {
        return String.valueOf(GET_BLOCK_NAME.getOrThrow().invoke(nmsBlock));
    }
    
    // Bukkit Item -> NMS Item
    private static final ThrowsOr<MethodHandle> GET_ITEM_BY_MATERIAL =
        ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getItem", NMS_ITEM, Material.class);
    
    public static Object getNmsItemByMaterial(Material material) throws Throwable
    {
        return GET_ITEM_BY_MATERIAL.getOrThrow().invoke(material);
    }
    
    // NMS Item -> String (name: translation key)
    private static final ThrowsOr<MethodHandle> GET_ITEM_NAME =
        ServerReflection.requireMethod(NMS_ITEM, "getName", String.class);
    
    public static String getNmsItemName(Object nmsItem) throws Throwable
    {
        return String.valueOf(GET_ITEM_NAME.getOrThrow().invoke(nmsItem));
    }
    
    // NMS ItemStack -> NMS EnumItemRarity
    private static final ThrowsOr<MethodHandle> GET_ITEM_RARITY_BY_ITEM_STACK =
        ServerReflection.requireMethod(NMS_ITEM, "i", NMS_ENUM_ITEM_RARITY, NMS_ITEM_STACK);
    
    public static Object getItemRarityOfNmsItemStack(Object nmsItem, Object nmsItemStack) throws Throwable
    {
        return GET_ITEM_RARITY_BY_ITEM_STACK.getOrThrow().invoke(nmsItem, nmsItemStack);
    }
    
    // NMS Item -> NMS EnumItemRarity
    private static final ThrowsOr<Field> NMS_ITEM_RARITY_FIELD =
        ServerReflection.requireField(NMS_ITEM, "a", NMS_ENUM_ITEM_RARITY);
    
    public static Object getItemRarityOfNmsItem(Object nmsItem) throws Throwable
    {
        return NMS_ITEM_RARITY_FIELD.getOrThrow().get(nmsItem);
    }
}
