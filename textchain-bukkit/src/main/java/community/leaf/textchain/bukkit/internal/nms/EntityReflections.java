/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.internal.nms;

import community.leaf.textchain.bukkit.internal.BukkitVersion;
import community.leaf.textchain.bukkit.internal.ServerReflection;
import community.leaf.textchain.bukkit.internal.ThrowsOr;
import org.bukkit.entity.EntityType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

final class EntityReflections
{
    private EntityReflections() { throw new UnsupportedOperationException(); }
    
    static final EntityReflection ENTITIES;
    
    static
    {
        ENTITIES = (BukkitVersion.getServerVersion().isAtLeast(1, 17)) ? new Impl_v1_17() : new Impl_v1_16();
    }
    
    /**
     * 1.x -> 1.16
     */
    private static class Impl_v1_16 implements EntityReflection
    {
        private static final Class<?> NMS_ENTITY_TYPES = ServerReflection.requireNmsClass("EntityTypes");
        
        // String -> Optional<NMS EntityTypes>
        private static final ThrowsOr<MethodHandle> nmsEntityTypesByName =
            ServerReflection.requireStaticMethod(NMS_ENTITY_TYPES, "a", Optional.class, String.class);
        
        // NMS EntityTypes -> String
        private static final ThrowsOr<MethodHandle> translationKeyByNmsEntityTypes =
            ServerReflection.requireMethod(NMS_ENTITY_TYPES, "f", String.class);
        
        @Override
        public String translationKey(EntityType type) throws Throwable
        {
            @NullOr String entityTypeName = type.getName();
            
            Object nmsEntityTypes =
                ((Optional<?>) nmsEntityTypesByName.getOrThrow().invoke(entityTypeName))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityTypeName));
                
            return String.valueOf(translationKeyByNmsEntityTypes.getOrThrow().invoke(nmsEntityTypes));
        }
    }
    
    /**
     * 1.17+
     */
    private static class Impl_v1_17 implements EntityReflection
    {
        private static final Class<?> NMS_ENTITY_TYPES = ServerReflection.requireNmsClass("world.entity.EntityTypes");
        
        // String -> Optional<NMS EntityTypes>
        private static final ThrowsOr<MethodHandle> nmsEntityTypesByName =
            ServerReflection.requireStaticMethod(NMS_ENTITY_TYPES, "a", Optional.class, String.class);
        
        // NMS EntityTypes -> String
        private static final ThrowsOr<MethodHandle> translationKeyByNmsEntityTypes =
            ServerReflection.requireMethod(NMS_ENTITY_TYPES, "g", String.class);
        
        @Override
        public String translationKey(EntityType type) throws Throwable
        {
            @NullOr String entityTypeName = type.getName();
            
            Object nmsEntityTypes =
                ((Optional<?>) nmsEntityTypesByName.getOrThrow().invoke(entityTypeName))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityTypeName));
                
            return String.valueOf(translationKeyByNmsEntityTypes.getOrThrow().invoke(nmsEntityTypes));
        }
    }
}
