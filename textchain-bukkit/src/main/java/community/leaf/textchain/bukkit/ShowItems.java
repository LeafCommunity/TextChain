package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowItem;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftReflection;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.invoke.MethodHandle;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class ShowItems
{
    private ShowItems() { throw new UnsupportedOperationException(); }
    
    static final Class<?> CRAFT_ITEM_STACK = MinecraftReflection.needCraftClass("inventory.CraftItemStack");
    
    static final Class<?> CRAFT_MAGIC_NUMBERS = MinecraftReflection.needCraftClass("util.CraftMagicNumbers");
    
    static final Class<?> NMS_ITEM = MinecraftReflection.needNmsClass("Item");
    
    static final Class<?> NMS_ITEM_STACK = MinecraftReflection.needNmsClass("ItemStack");
    
    static final Class<?> NMS_NBT_TAG_COMPOUND = MinecraftReflection.needNmsClass("NBTTagCompound");
    
    static final MethodHandle AS_NMS_COPY =
        Objects.requireNonNull(
            MinecraftReflection.findStaticMethod(CRAFT_ITEM_STACK, "asNMSCopy", NMS_ITEM_STACK, ItemStack.class),
            "Missing method: obc.inventory.CraftItemStack.asNMSCopy(ItemStack)"
        );
    
    static final MethodHandle GET_OR_CREATE_TAG =
        Objects.requireNonNull(
            MinecraftReflection.findMethod(NMS_ITEM_STACK, "getOrCreateTag", NMS_NBT_TAG_COMPOUND),
            "Missing method: nms.ItemStack.getOrCreateTag()"
        );
    
    static final MethodHandle GET_ITEM_BY_MATERIAL =
        Objects.requireNonNull(
            MinecraftReflection.findStaticMethod(CRAFT_MAGIC_NUMBERS, "getItem", NMS_ITEM, Material.class),
            "Missing method: obc.util.CraftMagicNumbers.getItem(Material)"
        );
    
    static final MethodHandle GET_ITEM_NAME =
        Objects.requireNonNull(
            MinecraftReflection.findMethod(NMS_ITEM, "getName", String.class),
            "Missing method: nms.Item.getName()"
        );
    
    public static BinaryTagHolder nbt(ItemStack item)
    {
        try { return BinaryTagHolder.of(String.valueOf(GET_OR_CREATE_TAG.invoke(AS_NMS_COPY.invoke(item)))); }
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
        try { return String.valueOf(GET_ITEM_NAME.invoke(GET_ITEM_BY_MATERIAL.invoke(material))); }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static String asTranslationKey(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return asProperName(item.getType());
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
    
    public static Component asName(ItemStack item)
    {
        return asDisplayName(item).orElseGet(() -> asTranslatable(item));
    }
    
    private static Consumer<TextChain> ifNotEmpty(String text)
    {
        return chain -> { if (!text.isEmpty()) { chain.then(text); }};
    }
    
    public static TextComponent asText(ItemStack item, String prefix, String suffix)
    {
        return TextChain.empty()
            .extra(chain -> chain.apply(ifNotEmpty(prefix)).then(asName(item)).apply(ifNotEmpty(suffix)))
            .hover(asHover(item))
            .asComponent();
    }
    
    public static TextComponent asText(ItemStack item)
    {
        return asText(item, "[", "]");
    }
    
    public static String asProperName(Material material)
    {
        return TranslationRegistry.INSTANCE.translate(asTranslationKey(material));
    }
    
    public static String asProperName(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        return asProperName(item.getType());
    }
}
