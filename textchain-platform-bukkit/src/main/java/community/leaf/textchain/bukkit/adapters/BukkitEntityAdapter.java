/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.bukkit.LegacyBukkitComponentSerializer;
import community.leaf.textchain.platforms.adapters.EntityAdapter;
import community.leaf.textchain.platforms.adapters.EntityTypeAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Optional;
import java.util.UUID;

class BukkitEntityAdapter implements EntityAdapter<EntityType, Entity>
{
    private final BukkitEntityTypeAdapter types;
    
    public BukkitEntityAdapter(BukkitEntityTypeAdapter types) { this.types = types; }
    
    @Override
    public EntityTypeAdapter<EntityType> types()
    {
        return types;
    }
    
    @Override
    public EntityType type(Entity entity)
    {
        return entity.getType();
    }
    
    @Override
    public UUID uuid(Entity entity)
    {
        return entity.getUniqueId();
    }
    
    @Override
    public Optional<Component> customName(Entity entity)
    {
         return Optional.ofNullable(entity.getCustomName())
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    @Override
    public void customName(Entity entity, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        entity.setCustomName(LegacyBukkitComponentSerializer.legacyHexSection().serialize(component));
    }
}
