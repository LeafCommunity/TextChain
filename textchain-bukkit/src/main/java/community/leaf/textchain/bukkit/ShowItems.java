package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.internal.ServerReflection;
import community.leaf.textchain.bukkit.internal.ThrowsOr;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowItem;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShowItems
{
    private ShowItems() { throw new UnsupportedOperationException(); }
    
    // These classes should exist as if importing them directly.
    
    static final Class<?> CRAFT_ITEM_STACK = ServerReflection.requireCraftClass("inventory.CraftItemStack");
    
    static final Class<?> CRAFT_MAGIC_NUMBERS = ServerReflection.requireCraftClass("util.CraftMagicNumbers");
    
    static final Class<?> NMS_ENUM_ITEM_RARITY = ServerReflection.requireNmsClass("EnumItemRarity");
    
    static final Class<?> NMS_BLOCK = ServerReflection.requireNmsClass("Block");
    
    static final Class<?> NMS_ITEM = ServerReflection.requireNmsClass("Item");
    
    static final Class<?> NMS_ITEM_STACK = ServerReflection.requireNmsClass("ItemStack");
    
    static final Class<?> NMS_NBT_TAG_COMPOUND = ServerReflection.requireNmsClass("NBTTagCompound");
    
    // Methods and fields that may or may not exist, but they most likely do in fact exist.
    // But let's not throw anything until called (like standard java behavior).
    
    static final ThrowsOr<MethodHandle> AS_NMS_COPY =
        ServerReflection.requireStaticMethod(CRAFT_ITEM_STACK, "asNMSCopy", NMS_ITEM_STACK, ItemStack.class);
    
    static final ThrowsOr<MethodHandle> GET_OR_CREATE_TAG =
        ServerReflection.requireMethod(NMS_ITEM_STACK, "getOrCreateTag", NMS_NBT_TAG_COMPOUND);
    
    static final ThrowsOr<MethodHandle> GET_BLOCK_BY_MATERIAL =
        ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getBlock", NMS_BLOCK, Material.class);
    
    static final ThrowsOr<MethodHandle> GET_BLOCK_NAME =
        ServerReflection.requireMethod(NMS_BLOCK, "i", String.class);
    
    static final ThrowsOr<MethodHandle> GET_ITEM_BY_MATERIAL =
        ServerReflection.requireStaticMethod(CRAFT_MAGIC_NUMBERS, "getItem", NMS_ITEM, Material.class);
        
    static final ThrowsOr<MethodHandle> GET_ITEM_NAME =
        ServerReflection.requireMethod(NMS_ITEM, "getName", String.class);
    
    static final ThrowsOr<MethodHandle> GET_ITEM_RARITY_BY_ITEM_STACK =
        ServerReflection.requireMethod(NMS_ITEM, "i", NMS_ENUM_ITEM_RARITY, NMS_ITEM_STACK);
    
    static final ThrowsOr<Field> NMS_ITEM_RARITY_FIELD =
        ServerReflection.requireField(NMS_ITEM, "a", NMS_ENUM_ITEM_RARITY);
    
    // Utility methods.
    
    public static Key itemKey(Material material)
    {
        return BukkitKeys.convertKey(material);
    }
    
    public static Key itemKey(ItemStack item)
    {
        return itemKey(item.getType());
    }
    
    public static BinaryTagHolder itemNbt(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        try
        {
            return BinaryTagHolder.of(String.valueOf(
                GET_OR_CREATE_TAG.getOrThrow().invoke(AS_NMS_COPY.getOrThrow().invoke(item))
            ));
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static HoverEvent<ShowItem> itemHover(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        ShowItem showItem = ShowItem.of(itemKey(item), item.getAmount(), itemNbt(item));
        return HoverEvent.showItem(showItem);
    }
    
    public static String itemTranslationKey(Material material)
    {
        Objects.requireNonNull(material, "material");
        
        try
        {
            if (material.isBlock())
            {
                Object nmsBlock = GET_BLOCK_BY_MATERIAL.getOrThrow().invoke(material);
                return String.valueOf(GET_BLOCK_NAME.getOrThrow().invoke(nmsBlock));
            }
            else
            {
                Object nmsItem = GET_ITEM_BY_MATERIAL.getOrThrow().invoke(material);
                return String.valueOf(GET_ITEM_NAME.getOrThrow().invoke(nmsItem));
            }
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static String itemTranslationKey(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return itemTranslationKey(item.getType());
    }
    
    public static TranslatableComponent itemTranslatable(Material material)
    {
        return Component.translatable(itemTranslationKey(material));
    }
    
    public static TranslatableComponent itemTranslatable(ItemStack item)
    {
        return Component.translatable(itemTranslationKey(item));
    }
    
    public static Optional<Component> itemDisplayName(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return Optional.ofNullable(item.getItemMeta())
            .filter(ItemMeta::hasDisplayName)
            .map(ItemMeta::getDisplayName)
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    public static Component itemDisplayOrTranslatableName(ItemStack item)
    {
        return itemDisplayName(item).orElseGet(() -> itemTranslatable(item));
    }
    
    public static TextComponent itemComponent(ItemStack item, String prefix, String suffix)
    {
        return TextChain.chain()
            .extra(chain -> {
                if (!prefix.isEmpty()) { chain.then(prefix); }
                chain.then(itemDisplayOrTranslatableName(item));
                if (!suffix.isEmpty()) { chain.then(suffix); }
            })
            .hover(itemHover(item))
            .asComponent();
    }
    
    public static TextComponent itemComponent(ItemStack item) { return itemComponent(item, "", ""); }
    
    public static TextComponent itemComponentInBrackets(ItemStack item) { return itemComponent(item, "[", "]"); }
    
    public static String itemClientName(Material material)
    {
        return TranslationRegistry.INSTANCE.translate(itemTranslationKey(material));
    }
    
    public static String itemClientName(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return itemClientName(item.getType());
    }
    
    public static ItemRarity rarity(Material material)
    {
        Objects.requireNonNull(material, "material");
        if (!material.isItem()) { return ItemRarity.COMMON; }
        
        try
        {
            Object nmsItem = GET_ITEM_BY_MATERIAL.getOrThrow().invoke(material);
            Object nmsRarity = NMS_ITEM_RARITY_FIELD.getOrThrow().get(nmsItem);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static ItemRarity rarity(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        
        Material material = item.getType();
        if (!material.isItem()) { return ItemRarity.COMMON; }
        
        try
        {
            Object nmsItem = GET_ITEM_BY_MATERIAL.getOrThrow().invoke(material);
            Object nmsItemStack = AS_NMS_COPY.getOrThrow().invoke(item);
            Object nmsRarity = GET_ITEM_RARITY_BY_ITEM_STACK.getOrThrow().invoke(nmsItem, nmsItemStack);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static @NullOr ItemMeta setMetaDisplayName(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        
        if (meta != null)
        {
            meta.setDisplayName(LegacyBukkitComponentSerializer.legacyHexSection().serialize(component));
        }
        return meta;
    }
    
    public static void setItemDisplayName(ItemStack item, ComponentLike componentLike)
    {
        Objects.requireNonNull(item, "item");
        item.setItemMeta(setMetaDisplayName(item.getItemMeta(), componentLike));
    }
    
    public static @NullOr ItemMeta setMetaLore(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        
        if (meta != null)
        {
            meta.setLore(
                Components.flattenExtraSplitByNewLine(component).stream()
                    .map(LegacyBukkitComponentSerializer.legacyHexSection()::serialize)
                    .collect(Collectors.toList())
            );
        }
        return meta;
    }
    
    public static void setItemLore(ItemStack item, ComponentLike componentLike)
    {
        Objects.requireNonNull(item, "item");
        item.setItemMeta(setMetaLore(item.getItemMeta(), componentLike));
    }
}
