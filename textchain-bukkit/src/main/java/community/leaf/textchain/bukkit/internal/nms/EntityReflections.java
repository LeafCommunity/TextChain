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
        ENTITIES = (BukkitVersion.getServerVersion().isAtLeast(1, 17)) ? null : new Impl_v1_16();
    }
    
    private static class Impl_v1_16 implements EntityReflection
    {
        private final Class<?> NMS_ENTITY_TYPES = ServerReflection.requireNmsClass("EntityTypes");
        
        // String -> Optional<NMS EntityTypes>
        private final ThrowsOr<MethodHandle> GET_ENTITY_TYPE_BY_NAME =
            ServerReflection.requireStaticMethod(NMS_ENTITY_TYPES, "a", Optional.class, String.class);
        
        private Optional<?> getEntityTypesByName(@NullOr String entityTypeName) throws Throwable
        {
            return ((Optional<?>) GET_ENTITY_TYPE_BY_NAME.getOrThrow().invoke(entityTypeName));
        }
        
        // NMS EntityTypes -> String
        private final ThrowsOr<MethodHandle> GET_TRANSLATION_KEY =
            ServerReflection.requireMethod(NMS_ENTITY_TYPES, "f", String.class);
        
        private String getTranslationKeyByNmsEntityTypes(Object nmsEntityTypes) throws Throwable
        {
            return String.valueOf(GET_TRANSLATION_KEY.getOrThrow().invoke(nmsEntityTypes));
        }
        
        @Override
        public String translationKey(EntityType type) throws Throwable
        {
            @NullOr String entityTypeName = type.getName();
        
            Object nmsEntityTypes = getEntityTypesByName(entityTypeName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityTypeName));
                
            return getTranslationKeyByNmsEntityTypes(nmsEntityTypes);
        }
    }
}
