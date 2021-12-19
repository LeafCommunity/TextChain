/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.internal.nms;

import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.bukkit.internal.Reflect;
import community.leaf.textchain.bukkit.internal.ThrowsOr;
import org.bukkit.Material;
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
    final ThrowsOr<MethodHandle> nmsNbtTagCompoundByNmsItemStack =
        Reflect.on(NMS_ITEM_STACK).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == NMS_NBT_TAG_COMPOUND)
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> method.getAnnotations().length == 0) // getTag() has @Nullable
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nmsNbtTagCompoundByNmsItemStack"
            )));
    
    // CraftMagicNumbers.getBlock(Material) -> NMS Block
    final ThrowsOr<MethodHandle> nmsBlockByMaterial =
        Reflect.on(CRAFT_MAGIC_NUMBERS).requireStaticMethod("getBlock", NMS_BLOCK, Material.class);
    
    // NMS Block#getDescriptionId() -> String (translation key)
    final ThrowsOr<MethodHandle> nameByNmsBlock =
        Reflect.on(NMS_BLOCK).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == String.class)
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> !method.getName().equals("toString"))
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nameByNmsBlock"
            )));
    
    // CraftMagicNumbers.getItem(Material) -> NMS Item
    final ThrowsOr<MethodHandle> nmsItemByMaterial =
        Reflect.on(CRAFT_MAGIC_NUMBERS).requireStaticMethod("getItem", NMS_ITEM, Material.class);
    
    // NMS Item#getDescriptionId() -> String (translation key)
    final ThrowsOr<MethodHandle> nameByNmsItem =
        Reflect.on(NMS_ITEM).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == String.class)
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> !method.getName().equals("toString"))
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nameByNmsItem"
            )));
    
    // NMS Item#getRarity(NMS ItemStack) -> NMS EnumItemRarity
    final ThrowsOr<MethodHandle> nmsRarityByNmsItemStack =
        Reflect.on(NMS_ITEM).methods()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == NMS_ENUM_ITEM_RARITY)
            .filter(method -> method.getParameterCount() == 1)
            .filter(method -> method.getParameterTypes()[0] == NMS_ITEM_STACK)
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nmsRarityByNmsItemStack"
            )));
    
    // NMS Item#rarity -> NMS EnumItemRarity
    final ThrowsOr<Field> nmsItemRarityByNmsItem =
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
