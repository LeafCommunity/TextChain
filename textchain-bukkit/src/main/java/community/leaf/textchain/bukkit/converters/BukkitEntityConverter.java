package community.leaf.textchain.bukkit.converters;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.bukkit.LegacyBukkitComponentSerializer;
import community.leaf.textchain.platforms.EntityConverter;
import community.leaf.textchain.platforms.EntityTypeConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Optional;
import java.util.UUID;

public class BukkitEntityConverter implements EntityConverter<EntityType, Entity>
{
    private final BukkitEntityTypeConverter types;
    
    public BukkitEntityConverter(BukkitEntityTypeConverter types) { this.types = types; }
    
    @Override
    public EntityTypeConverter<EntityType> types()
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
