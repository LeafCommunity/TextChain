/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.internal.nms;

import community.leaf.textchain.platforms.bukkit.internal.Reflect;
import community.leaf.textchain.platforms.bukkit.internal.ThrowsOr;
import org.bukkit.entity.EntityType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Modifier;
import java.util.Optional;

final class EntityReflectionImpl implements EntityReflection
{
    static final ThrowsOr<EntityReflection> INSTANCE = ThrowsOr.result(EntityReflectionImpl::new);
    
    final Class<?> NMS_ENTITY_TYPES =
        Reflect.inMinecraft().requireAnyClass(
            "EntityTypes", "world.entity.EntityTypes"
        );
    
    final ThrowsOr<MethodHandle> nmsEntityTypesByName =
        Reflect.on(NMS_ENTITY_TYPES).methods()
            .filter(method -> Modifier.isStatic(method.getModifiers()))
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .filter(method -> method.getReturnType() == Optional.class)
            .filter(method -> method.getParameterCount() == 1)
            .filter(method -> method.getParameterTypes()[0] == String.class)
            .findFirst()
            .flatMap(Reflect::unreflect)
            .map(ThrowsOr::value)
            .orElseGet(() -> ThrowsOr.raise(new IllegalStateException(
                "Could not find method matching nmsEntityTypesByName"
            )));
    
    @Override
    public String translationKey(EntityType type) throws Throwable
    {
        @NullOr String entityTypeName = type.getName();
        
        Object nmsEntityTypes =
            ((Optional<?>) nmsEntityTypesByName.getOrThrow().invoke(entityTypeName))
                .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityTypeName));
            
        // toString() on an EntityTypes instance returns its translation key
        return String.valueOf(nmsEntityTypes);
    }
}
