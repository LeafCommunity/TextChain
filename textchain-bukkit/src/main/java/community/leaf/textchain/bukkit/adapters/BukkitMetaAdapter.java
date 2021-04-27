package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.bukkit.LegacyBukkitComponentSerializer;
import community.leaf.textchain.platforms.adapters.MetaAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class BukkitMetaAdapter implements MetaAdapter<ItemMeta>
{
    @Override
    public Optional<Component> displayName(@NullOr ItemMeta meta)
    {
        return Optional.ofNullable(meta)
            .filter(ItemMeta::hasDisplayName)
            .map(ItemMeta::getDisplayName)
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    @Override
    public @NullOr ItemMeta displayName(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        if (meta != null) { meta.setDisplayName(LegacyBukkitComponentSerializer.legacyHexSection().serialize(component)); }
        return meta;
    }
    
    @Override
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
    public @NullOr ItemMeta lore(@NullOr ItemMeta meta, ComponentLike componentLike)
    {
        return lore(meta, Components.flattenExtraSplitByNewLine(Components.safelyAsComponent(componentLike)));
    }
}