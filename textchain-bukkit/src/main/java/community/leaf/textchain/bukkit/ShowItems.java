package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.util.ServerReflection;
import community.leaf.textchain.bukkit.util.ThrowsOr;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    
    public static BinaryTagHolder nbt(ItemStack item)
    {
        try
        {
            return BinaryTagHolder.of(String.valueOf(
                GET_OR_CREATE_TAG.getOrThrow().invoke(AS_NMS_COPY.getOrThrow().invoke(item))
            ));
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static HoverEvent<ShowItem> asHover(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        Key key = Key.key(item.getType().getKey().toString(), ':');
        ShowItem showItem = ShowItem.of(key, item.getAmount(), nbt(item));
        return HoverEvent.showItem(showItem);
    }
    
    public static String asTranslationKey(Material material)
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
    
    public static String asTranslationKey(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return asTranslationKey(item.getType());
    }
    
    public static TranslatableComponent asTranslatable(Material material)
    {
        return Component.translatable(asTranslationKey(material));
    }
    
    public static TranslatableComponent asTranslatable(ItemStack item)
    {
        return Component.translatable(asTranslationKey(item));
    }
    
    public static Optional<Component> asDisplayName(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return Optional.ofNullable(item.getItemMeta())
            .filter(ItemMeta::hasDisplayName)
            .map(ItemMeta::getDisplayName)
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    public static Component asDisplayOrTranslatableName(ItemStack item)
    {
        return asDisplayName(item).orElseGet(() -> asTranslatable(item));
    }
    
    public static TextComponent asComponent(ItemStack item, String prefix, String suffix)
    {
        return TextChain.empty()
            .extra(chain -> {
                if (!prefix.isEmpty()) { chain.then(prefix); }
                chain.then(asDisplayOrTranslatableName(item));
                if (!suffix.isEmpty()) { chain.then(suffix); }
            })
            .hover(asHover(item))
            .asComponent();
    }
    
    public static TextComponent asComponent(ItemStack item) { return asComponent(item, "", ""); }
    
    public static TextComponent asComponentInBrackets(ItemStack item) { return asComponent(item, "[", "]"); }
    
    public static String asClientName(Material material)
    {
        return TranslationRegistry.INSTANCE.translate(asTranslationKey(material));
    }
    
    public static String asClientName(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return asClientName(item.getType());
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
    
    public static @NullOr ItemMeta setDisplayName(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        if (meta != null) { meta.setDisplayName(LegacyBukkitComponentSerializer.legacyHexSection().serialize(component)); }
        return meta;
    }
    
    public static void setDisplayName(ItemStack item, ComponentLike componentLike)
    {
        Objects.requireNonNull(item, "item");
        item.setItemMeta(setDisplayName(item.getItemMeta(), componentLike));
    }
    
    public static @NullOr ItemMeta setLore(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        
        if (meta != null)
        {
            List<String> lore = new ArrayList<>();
            for (Component extra : Components.flattenExtraSplitByNewLine(component))
            {
                lore.add(LegacyBukkitComponentSerializer.legacyHexSection().serialize(extra));
            }
            meta.setLore(lore);
        }
        
        return meta;
    }
    
    public static void setLore(ItemStack item, ComponentLike componentLike)
    {
        Objects.requireNonNull(item, "item");
        item.setItemMeta(setLore(item.getItemMeta(), componentLike));
    }
}
