package community.leaf.textchain.bukkit.converters;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.bukkit.LegacyBukkitComponentSerializer;
import community.leaf.textchain.platforms.ItemConverter;
import community.leaf.textchain.platforms.ItemTypeConverter;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static community.leaf.textchain.bukkit.internal.nms.ItemReflection.*;

public class BukkitItemConverter implements ItemConverter<Material, ItemStack>
{
    private final BukkitMaterialConverter materials;
    
    public BukkitItemConverter(BukkitMaterialConverter materials) { this.materials = materials; }
    
    @Override
    public ItemTypeConverter<Material> types()
    {
        return materials;
    }
    
    @Override
    public Material type(ItemStack item)
    {
        return item.getType();
    }
    
    @Override
    public int amount(ItemStack item)
    {
        return item.getAmount();
    }
    
    @Override
    public BinaryTagHolder nbt(ItemStack item)
    {
        try { return BinaryTagHolder.of(String.valueOf(getOrCreateTag(asNmsCopy(item)))); }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public Optional<Component> displayName(@NullOr ItemMeta meta)
    {
        return Optional.ofNullable(meta)
            .filter(ItemMeta::hasDisplayName)
            .map(ItemMeta::getDisplayName)
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    @Override
    public Optional<Component> displayName(ItemStack item)
    {
        return displayName(item.getItemMeta());
    }
    
    public @NullOr ItemMeta displayName(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        if (meta != null) { meta.setDisplayName(LegacyBukkitComponentSerializer.legacyHexSection().serialize(component)); }
        return meta;
    }
    
    @Override
    public void displayName(ItemStack item, ComponentLike componentLike)
    {
        item.setItemMeta(displayName(item.getItemMeta(), componentLike));
    }
    
    public List<Component> lore(@NullOr ItemMeta meta)
    {
        List<Component> lore = new ArrayList<>();
        if (meta == null) { return lore; }
        
        @NullOr List<String> existing = meta.getLore();
        if (existing == null || existing.isEmpty()) { return lore; }
        
        return existing.stream()
            .map(LegacyBukkitComponentSerializer.legacyHexSection()::deserialize)
            .collect(Collectors.toCollection(() -> lore));
    }
    
    @Override
    public List<Component> lore(ItemStack item)
    {
        return lore(item.getItemMeta());
    }
    
    public @NullOr ItemMeta lore(@NullOr ItemMeta meta, List<Component> lore)
    {
        if (meta != null)
        {
            meta.setLore(lore.stream()
                .map(LegacyBukkitComponentSerializer.legacyHexSection()::serialize)
                .collect(Collectors.toCollection(ArrayList::new))
            );
        }
        return meta;
    }
    
    @Override
    public void lore(ItemStack item, List<Component> lore)
    {
        item.setItemMeta(lore(item.getItemMeta(), lore));
    }
    
    public @NullOr ItemMeta lore(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        return lore(meta, Components.flattenExtraSplitByNewLine(Components.safelyAsComponent(componentLike)));
    }
    
    @Override
    public ItemRarity rarity(ItemStack item)
    {
        Material type = item.getType();
        if (!type.isItem()) { return ItemRarity.COMMON; }
        
        try
        {
            Object nmsItem = getNmsItemByMaterial(type);
            Object nmsItemStack = asNmsCopy(item);
            Object nmsRarity = getItemRarityOfNmsItemStack(nmsItem, nmsItemStack);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
}
