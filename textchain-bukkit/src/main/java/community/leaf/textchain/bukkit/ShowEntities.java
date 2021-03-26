package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.util.ServerReflection;
import community.leaf.textchain.bukkit.util.ThrowsOr;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowEntity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Entity;
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
    
    public static HoverEvent<ShowEntity> asHover(Entity entity, @NullOr ComponentLike customName)
    {
        Objects.requireNonNull(entity, "entity");
        
        Key key = Key.key(entity.getType().getKey().toString(), ':');
        @NullOr Component name = (customName == null) ? null : customName.asComponent();
        
        return HoverEvent.showEntity(ShowEntity.of(key, entity.getUniqueId(), name));
    }
    
    public static HoverEvent<ShowEntity> asHover(Entity entity)
    {
        return asHover(entity, asCustomName(entity).orElse(null));
    }
    
    public static String asTranslationKey(Entity entity)
    {
        Objects.requireNonNull(entity, "entity");
        @NullOr String entityType = entity.getType().getName();
        
        try
        {
            Object nmsEntityType =
                ((Optional<?>) GET_ENTITY_TYPE_BY_NAME.getOrThrow().invoke(entityType))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityType));
            
            return String.valueOf(GET_TRANSLATION_KEY.getOrThrow().invoke(nmsEntityType));
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    public static TranslatableComponent asTranslatable(Entity entity)
    {
        return Component.translatable(asTranslationKey(entity));
    }
    
    public static Optional<Component> asCustomName(Entity entity)
    {
        Objects.requireNonNull(entity, "entity");
        return Optional.ofNullable(entity.getCustomName())
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    public static Component asCustomOrTranslatableName(Entity entity)
    {
        return asCustomName(entity).orElseGet(() -> asTranslatable(entity));
    }
    
    public static TextComponent asComponent(Entity entity, String prefix, String suffix)
    {
        return TextChain.empty()
            .extra(chain -> {
                if (!prefix.isEmpty()) { chain.then(prefix); }
                chain.then(asCustomOrTranslatableName(entity));
                if (!suffix.isEmpty()) { chain.then(suffix); }
            })
            .hover(asHover(entity))
            .asComponent();
    }
    
    public static TextComponent asComponent(Entity entity) { return asComponent(entity, "", ""); }
    
    public static TextComponent asComponentInBrackets(Entity entity) { return asComponent(entity, "[", "]"); }
    
    public static void setCustomName(Entity entity, ComponentLike componentLike)
    {
        Objects.requireNonNull(entity, "entity");
        Component component = Components.safelyAsComponent(componentLike);
        entity.setCustomName(LegacyBukkitComponentSerializer.legacyHexSection().serialize(component));
    }
}
