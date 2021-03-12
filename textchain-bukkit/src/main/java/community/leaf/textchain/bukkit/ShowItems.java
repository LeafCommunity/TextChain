package community.leaf.textchain.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftReflection;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

import java.lang.invoke.MethodHandle;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class ShowItems
{
    private ShowItems() {}
    
    static final Class<?> CRAFT_ITEM_STACK = MinecraftReflection.needCraftClass("inventory.CraftItemStack");
    
    static final Class<?> NMS_ITEM_STACK = MinecraftReflection.needNmsClass("ItemStack");
    
    static final Class<?> NMS_NBT_TAG_COMPOUND = MinecraftReflection.needNmsClass("NBTTagCompound");
    
    static final MethodHandle AS_NMS_COPY =
        Objects.requireNonNull(
            MinecraftReflection.findStaticMethod(CRAFT_ITEM_STACK, "asNMSCopy", NMS_ITEM_STACK, ItemStack.class),
            "Missing method: CraftItemStack.asNMSCopy(ItemStack)"
        );
    
    static final MethodHandle GET_OR_CREATE_TAG =
        Objects.requireNonNull(
            MinecraftReflection.findMethod(NMS_ITEM_STACK, "getOrCreateTag", NMS_NBT_TAG_COMPOUND),
            "Missing method: nms.ItemStack.getOrCreateTag()"
        );
    
    public static HoverEventSource<Component> asHover(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        
        try
        {
            ItemTag itemTag = ItemTag.ofNbt(String.valueOf(GET_OR_CREATE_TAG.invoke(AS_NMS_COPY.invoke(item))));
            Content content = new Item(item.getType().getKey().toString(), item.getAmount(), itemTag);
            HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, content);
            
            return BungeeComponentSerializer.get().deserialize(new ComponentBuilder().event(event).create());
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
        
        return Component.empty();
    }
    
    public static Component asName(ItemStack item)
    {
        Objects.requireNonNull(item, "item");
        @NullOr ItemMeta meta = item.getItemMeta();
    
        if (meta != null && meta.hasDisplayName())
        {
            return LegacyComponentSerializer.legacySection().deserialize(meta.getDisplayName());
        }
        else
        {
            String category = item.getType().isBlock() ? "block" : "item";
            String key = category + ".minecraft." + item.getType().name().toLowerCase();
            return Component.translatable(key);
        }
    }
    
    public static TextComponent asText(ItemStack item, String prefix, String suffix)
    {
        return Component.text()
            .hoverEvent(asHover(item))
            .append(Component.text(Objects.requireNonNull(prefix, "prefix")))
            .append(asName(item))
            .append(Component.text(Objects.requireNonNull(suffix, "suffix")))
            .build();
    }
    
    public static TextComponent asText(ItemStack item)
    {
        return asText(item, "[", "]");
    }
}
