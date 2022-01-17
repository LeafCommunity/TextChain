/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.internal.nms;

import community.leaf.textchain.platforms.ItemRarity;
import community.leaf.textchain.platforms.bukkit.internal.Reflect;
import community.leaf.textchain.platforms.bukkit.internal.ThrowsOr;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

final class ItemReflectionImpl implements ItemReflection
{
    static final ThrowsOr<ItemReflection> INSTANCE = ThrowsOr.result(ItemReflectionImpl::new);
    
    final Class<?> CRAFT_ITEM_STACK =
        Reflect.inCraftBukkit().requireAnyClass("inventory.CraftItemStack");
    
    final Class<?> CRAFT_MAGIC_NUMBERS =
        Reflect.inCraftBukkit().requireAnyClass("util.CraftMagicNumbers");
    
    final Class<?> NMS_ENUM_ITEM_RARITY =
        Reflect.inMinecraft().requireAnyClass("EnumItemRarity", "world.item.EnumItemRarity");
    
    final Class<?> NMS_BLOCK =
        Reflect.inMinecraft().requireAnyClass("Block", "world.level.block.Block");
    
    final Class<?> NMS_ITEM =
        Reflect.inMinecraft().requireAnyClass("Item", "world.item.Item");
    
    final Class<?> NMS_ITEM_STACK =
        Reflect.inMinecraft().requireAnyClass("ItemStack", "world.item.ItemStack");
    
    final Class<?> NMS_NBT_TAG_COMPOUND =
        Reflect.inMinecraft().requireAnyClass("NBTTagCompound", "nbt.NBTTagCompound");
    
    // CraftItemStack.asNMSCopy(ItemStack) -> NMS ItemStack
    final ThrowsOr<MethodHandle> nmsItemStackByBukkitCopy =
        Reflect.on(CRAFT_ITEM_STACK).requireStaticMethod("asNMSCopy", NMS_ITEM_STACK, ItemStack.class);
    
    // NMS ItemStack#getOrCreateTag() -> NMS NbtCompoundTag
    final ThrowsOr<MethodHandle> nmsNbtTagCompoundOfNmsItemStack =
        Reflect.on(NMS_ITEM_STACK).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == NMS_NBT_TAG_COMPOUND)
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> method.getAnnotations().length == 0) // getTag() has @Nullable
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nmsNbtTagCompoundOfNmsItemStack"
            )));
    
    // NMS ItemStack#getItem() -> NMS Item
    final ThrowsOr<MethodHandle> nmsItemOfNmsItemStack =
        Reflect.on(NMS_ITEM_STACK).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == NMS_ITEM)
            .filter(method -> method.getParameterCount() == 0)
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nmsItemOfNmsItemStack"
            )));
    
    // CraftMagicNumbers.getBlock(Material) -> NMS Block
    final ThrowsOr<MethodHandle> nmsBlockOfMaterial =
        Reflect.on(CRAFT_MAGIC_NUMBERS).requireStaticMethod("getBlock", NMS_BLOCK, Material.class);
    
    // NMS Block#getDescriptionId() -> String (translation key)
    final ThrowsOr<MethodHandle> nameOfNmsBlock =
        Reflect.on(NMS_BLOCK).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == String.class)
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> !method.getName().equals("toString"))
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nameOfNmsBlock"
            )));
    
    // CraftMagicNumbers.getItem(Material) -> NMS Item
    final ThrowsOr<MethodHandle> nmsItemOfMaterial =
        Reflect.on(CRAFT_MAGIC_NUMBERS).requireStaticMethod("getItem", NMS_ITEM, Material.class);
    
    // NMS Item#getDescriptionId() -> String (translation key)
    final ThrowsOr<MethodHandle> translationKeyOfNmsItem =
        Reflect.on(NMS_ITEM).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == String.class)
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> !method.getName().equals("toString"))
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching translationKeyOfNmsItem"
            )));
    
    // NMS Item#getDescriptionId(NMS ItemStack) -> String (translation key)
    final ThrowsOr<MethodHandle> translationKeyOfNmsItemByNmsItemStack =
        Reflect.on(NMS_ITEM).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == String.class)
            .filter(method -> method.getParameterCount() == 1)
            .filter(method -> method.getParameterTypes()[0] == NMS_ITEM_STACK)
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching translationKeyOfNmsItemByNmsItemStack"
            )));
    
    // NMS Item#getRarity(NMS ItemStack) -> NMS EnumItemRarity
    final ThrowsOr<MethodHandle> nmsRarityOfNmsItemByNmsItemStack =
        Reflect.on(NMS_ITEM).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == NMS_ENUM_ITEM_RARITY)
            .filter(method -> method.getParameterCount() == 1)
            .filter(method -> method.getParameterTypes()[0] == NMS_ITEM_STACK)
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nmsRarityOfNmsItemByNmsItemStack"
            )));
    
    // NMS Item#rarity -> NMS EnumItemRarity
    final ThrowsOr<Field> nmsItemRarityOfNmsItem =
        Reflect.on(NMS_ITEM).fields()
            .filter(field -> field.getType() == NMS_ENUM_ITEM_RARITY)
            .findFirst()
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find field matching nmsItemRarityByNmsItem"
            )));
    
    @Override
    public String compoundTag(ItemStack item) throws Throwable
    {
        Object nmsItemStack = nmsItemStackByBukkitCopy.getOrThrow().invoke(item);
        Object nmsNbtTagCompound = nmsNbtTagCompoundOfNmsItemStack.getOrThrow().invoke(nmsItemStack);
        return String.valueOf(nmsNbtTagCompound);
    }
    
    @Override
    public String translationKey(Material type) throws Throwable
    {
        if (type.isBlock())
        {
            Object nmsBlock = nmsBlockOfMaterial.getOrThrow().invoke(type);
            return String.valueOf(nameOfNmsBlock.getOrThrow().invoke(nmsBlock));
        }
        else
        {
            Object nmsItem = nmsItemOfMaterial.getOrThrow().invoke(type);
            return String.valueOf(translationKeyOfNmsItem.getOrThrow().invoke(nmsItem));
        }
    }
    
    @Override
    public String translationKey(ItemStack item) throws Throwable
    {
        Object nmsItemStack = nmsItemStackByBukkitCopy.getOrThrow().invoke(item);
        Object nmsItem = nmsItemOfNmsItemStack.getOrThrow().invoke(nmsItemStack);
        return String.valueOf(translationKeyOfNmsItemByNmsItemStack.getOrThrow().invoke(nmsItem, nmsItemStack));
    }

    @Override
    public ItemRarity rarity(Material type) throws Throwable
    {
        Object nmsItem = nmsItemOfMaterial.getOrThrow().invoke(type);
        Object nmsRarity = nmsItemRarityOfNmsItem.getOrThrow().get(nmsItem);
        return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
    }

    @Override
    public ItemRarity rarity(ItemStack item) throws Throwable
    {
        Object nmsItem = nmsItemOfMaterial.getOrThrow().invoke(item.getType());
        Object nmsItemStack = nmsItemStackByBukkitCopy.getOrThrow().invoke(item);
        Object nmsRarity = nmsRarityOfNmsItemByNmsItemStack.getOrThrow().invoke(nmsItem, nmsItemStack);
        return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
    }
}
