package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowItem;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftReflection;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.util.RGBLike;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class ShowItems
{
    private ShowItems() { throw new UnsupportedOperationException(); }
    
    static final Class<?> CRAFT_ITEM_STACK = MinecraftReflection.needCraftClass("inventory.CraftItemStack");
    
    static final Class<?> CRAFT_MAGIC_NUMBERS = MinecraftReflection.needCraftClass("util.CraftMagicNumbers");
    
    static final Class<?> NMS_ENUM_ITEM_RARITY = MinecraftReflection.needNmsClass("EnumItemRarity");
    
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
    
    static final MethodHandle GET_ITEM_RARITY =
        Objects.requireNonNull(
            MinecraftReflection.findMethod(NMS_ITEM, "i", NMS_ENUM_ITEM_RARITY, NMS_ITEM_STACK),
            "Missing method: nms.Item.i(nms.ItemStack)"
        );
    
    static final Field NMS_ITEM_RARITY =
        Objects.requireNonNull(
            MinecraftReflection.findField(NMS_ITEM, "a", NMS_ENUM_ITEM_RARITY),
            "Missing field: nms.Item.a"
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
    
    private static String fallbackTranslationKey(Material material)
    {
        return ((material.isBlock()) ? "block" : "item") + ".minecraft." + material.name().toLowerCase();
    }
    
    public static String asTranslationKey(Material material)
    {
        Objects.requireNonNull(material, "material");
        try
        {
            @NullOr Object nmsItem = GET_ITEM_BY_MATERIAL.invoke(material); // can return null apparently?
            return (nmsItem != null) ? String.valueOf(GET_ITEM_NAME.invoke(nmsItem)) : fallbackTranslationKey(material);
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
    
    public static Component asName(ItemStack item)
    {
        return asDisplayName(item).orElseGet(() -> asTranslatable(item));
    }
    
    private static Consumer<TextChain> ifNotEmpty(String text)
    {
        return chain -> { if (!text.isEmpty()) { chain.then(text); }};
    }
    
    public static TextComponent asComponent(ItemStack item, String prefix, String suffix)
    {
        return TextChain.empty()
            .extra(chain -> chain.apply(ifNotEmpty(prefix)).then(asName(item)).apply(ifNotEmpty(suffix)))
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
    
    public static Rarity rarity(Material material)
    {
        Objects.requireNonNull(material, "material");
        if (!material.isItem()) { return Rarity.COMMON; }
        
        try
        {
            Object nmsItem = GET_ITEM_BY_MATERIAL.invoke(material);
            Object nmsRarity = NMS_ITEM_RARITY.get(nmsItem);
            return Rarity.resolveByNameOrCommon(String.valueOf(nmsRarity));
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static Rarity rarity(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        
        Material material = item.getType();
        if (!material.isItem()) { return Rarity.COMMON; }
        
        try
        {
            Object nmsItem = GET_ITEM_BY_MATERIAL.invoke(material);
            Object nmsItemStack = AS_NMS_COPY.invoke(item);
            Object nmsRarity = GET_ITEM_RARITY.invoke(nmsItem, nmsItemStack);
            return Rarity.resolveByNameOrCommon(String.valueOf(nmsRarity));
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public enum Rarity implements ComponentLike, RGBLike
    {
        COMMON(NamedTextColor.WHITE),
        UNCOMMON(NamedTextColor.YELLOW),
        RARE(NamedTextColor.AQUA),
        EPIC(NamedTextColor.LIGHT_PURPLE);
        
        private final NamedTextColor color;
        private final TextComponent component;
        
        Rarity(NamedTextColor color)
        {
            this.color = color;
            this.component = Component.text().content(name()).color(color).build();
        }
        
        public NamedTextColor getColor() { return color; }
        
        @Override
        public Component asComponent() { return component; }
        
        @Override
        public int red() { return color.red(); }
    
        @Override
        public int green() { return color.green(); }
    
        @Override
        public int blue() { return color.blue(); }
    
        public static Rarity resolveByNameOrCommon(String name)
        {
            for (Rarity rarity : values())
            {
                if (rarity.name().equalsIgnoreCase(name)) { return rarity; }
            }
            return COMMON;
        }
    }
}
