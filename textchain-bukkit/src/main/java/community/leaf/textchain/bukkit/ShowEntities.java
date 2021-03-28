package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.internal.ServerReflection;
import community.leaf.textchain.bukkit.internal.ThrowsOr;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowEntity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.lang.invoke.MethodHandle;
import java.util.Objects;
import java.util.Optional;

public class ShowEntities
{
    private ShowEntities() { throw new UnsupportedOperationException(); }
    
    static final Class<?> NMS_ENTITY_TYPES = ServerReflection.requireNmsClass("EntityTypes");
    
    static final ThrowsOr<MethodHandle> GET_ENTITY_TYPE_BY_NAME =
        ServerReflection.requireStaticMethod(NMS_ENTITY_TYPES, "a", Optional.class, String.class);
    
    static final ThrowsOr<MethodHandle> GET_TRANSLATION_KEY =
        ServerReflection.requireMethod(NMS_ENTITY_TYPES, "f", String.class);
    
    public static Key entityKey(EntityType type)
    {
        return BukkitKeys.convertKey(type);
    }
    
    public static Key entityKey(Entity entity)
    {
        return entityKey(entity.getType());
    }
    
    public static HoverEvent<ShowEntity> entityHover(Entity entity, @NullOr ComponentLike customName)
    {
        Objects.requireNonNull(entity, "entity");
        @NullOr Component name = (customName == null) ? null : Components.safelyAsComponent(customName);
        return HoverEvent.showEntity(ShowEntity.of(entityKey(entity), entity.getUniqueId(), name));
    }
    
    public static HoverEvent<ShowEntity> entityHover(Entity entity)
    {
        return entityHover(entity, entityCustomName(entity).orElse(null));
    }
    
    public static String entityTranslationKey(EntityType type)
    {
        Objects.requireNonNull(type, "type");
        @NullOr String entityType = type.getName();
        
        try
        {
            Object nmsEntityType =
                ((Optional<?>) GET_ENTITY_TYPE_BY_NAME.getOrThrow().invoke(entityType))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityType));
            
            return String.valueOf(GET_TRANSLATION_KEY.getOrThrow().invoke(nmsEntityType));
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static String entityTranslationKey(Entity entity)
    {
        return entityTranslationKey(entity.getType());
    }
    
    public static TranslatableComponent entityTranslatable(EntityType type)
    {
        return Component.translatable(entityTranslationKey(type));
    }
    
    public static TranslatableComponent entityTranslatable(Entity entity)
    {
        return entityTranslatable(entity.getType());
    }
    
    public static Optional<Component> entityCustomName(Entity entity)
    {
        Objects.requireNonNull(entity, "entity");
        return Optional.ofNullable(entity.getCustomName())
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    public static Component entityCustomOrTranslatableName(Entity entity)
    {
        return entityCustomName(entity).orElseGet(() -> entityTranslatable(entity));
    }
    
    public static TextComponent entityComponent(Entity entity, String prefix, String suffix)
    {
        return TextChain.chain()
            .extra(chain -> {
                if (!prefix.isEmpty()) { chain.then(prefix); }
                chain.then(entityCustomOrTranslatableName(entity));
                if (!suffix.isEmpty()) { chain.then(suffix); }
            })
            .hover(entityHover(entity))
            .asComponent();
    }
    
    public static TextComponent entityComponent(Entity entity) { return entityComponent(entity, "", ""); }
    
    public static TextComponent entityComponentInBrackets(Entity entity) { return entityComponent(entity, "[", "]"); }
    
    public static void setEntityCustomName(Entity entity, ComponentLike componentLike)
    {
        Objects.requireNonNull(entity, "entity");
        Component component = Components.safelyAsComponent(componentLike);
        entity.setCustomName(LegacyBukkitComponentSerializer.legacyHexSection().serialize(component));
    }
}
