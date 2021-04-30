/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bukkit.internal.nms;

import community.leaf.textchain.bukkit.internal.ServerReflection;
import community.leaf.textchain.bukkit.internal.ThrowsOr;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class EntityReflection
{
    private EntityReflection() { throw new UnsupportedOperationException(); }
    
    private static final Class<?> NMS_ENTITY_TYPES = ServerReflection.requireNmsClass("EntityTypes");
    
    // String -> Optional<NMS EntityTypes>
    private static final ThrowsOr<MethodHandle> GET_ENTITY_TYPE_BY_NAME =
        ServerReflection.requireStaticMethod(NMS_ENTITY_TYPES, "a", Optional.class, String.class);
    
    public static Optional<?> getEntityTypesByName(String entityTypeName) throws Throwable
    {
        return ((Optional<?>) GET_ENTITY_TYPE_BY_NAME.getOrThrow().invoke(entityTypeName));
    }
    
    // NMS EntityTypes -> String
    private static final ThrowsOr<MethodHandle> GET_TRANSLATION_KEY =
        ServerReflection.requireMethod(NMS_ENTITY_TYPES, "f", String.class);
    
    public static String getTranslationKeyByNmsEntityTypes(Object nmsEntityTypes) throws Throwable
    {
        return String.valueOf(GET_TRANSLATION_KEY.getOrThrow().invoke(nmsEntityTypes));
    }
}
