/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.adapters;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.platforms.adapters.MetaAdapter;
import community.leaf.textchain.platforms.bukkit.LegacyBukkitComponentSerializer;
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
