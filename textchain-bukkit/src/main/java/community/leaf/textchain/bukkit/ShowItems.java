package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowItem;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftReflection;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

import java.lang.invoke.MethodHandle;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class ShowItems
{
    private ShowItems() { throw new UnsupportedOperationException(); }
    
    static final Class<?> CRAFT_ITEM_STACK = MinecraftReflection.needCraftClass("inventory.CraftItemStack");
    
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
    
    public static Component asName(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        @NullOr ItemMeta meta = item.getItemMeta();
    
        if (meta != null && meta.hasDisplayName())
        {
            return LegacyComponentSerializer.legacySection().deserialize(meta.getDisplayName());
        }
        
        String category = item.getType().isBlock() ? "block" : "item";
        String key = category + ".minecraft." + item.getType().name().toLowerCase();
        return Component.translatable(key);
    }
    
    public static TextComponent asText(ItemStack item, String prefix, String suffix)
    {
        return TextChain.empty()
            .extra(chain -> chain.then(prefix).then(asName(item)).then(suffix))
            .hover(asHover(item))
            .asComponent();
    }
    
    public static TextComponent asText(ItemStack item)
    {
        return asText(item, "[", "]");
    }
}
