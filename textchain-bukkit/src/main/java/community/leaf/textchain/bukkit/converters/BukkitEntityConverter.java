package community.leaf.textchain.bukkit.converters;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.bukkit.LegacyBukkitComponentSerializer;
import community.leaf.textchain.bukkit.internal.nms.EntityReflection;
import community.leaf.textchain.platforms.EntityConverter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Optional;
import java.util.UUID;

public class BukkitEntityConverter implements EntityConverter<EntityType, Entity>
{
    private final NamespacedKeyConverter keys;
    
    public BukkitEntityConverter(NamespacedKeyConverter keys) { this.keys = keys; }
    
    @Override
    public EntityType typeOfEntity(Entity entity)
    {
        return entity.getType();
    }
    
    @Override
    public UUID uuid(Entity entity)
    {
        return entity.getUniqueId();
    }
    
    @Override
    public Key typeKey(EntityType type)
    {
        return keys.key(type::getKey);
    }
    
    @Override
    public String typeTranslationKey(EntityType type)
    {
        @NullOr String entityTypeName = type.getName();
        
        try
        {
            Object nmsEntityTypes = EntityReflection.getEntityTypesByName(entityTypeName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityTypeName));
            
            return EntityReflection.getTranslationKeyByNmsEntityTypes(nmsEntityTypes);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
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
