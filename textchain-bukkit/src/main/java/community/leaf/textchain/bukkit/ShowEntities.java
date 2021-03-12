package community.leaf.textchain.bukkit;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowEntity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Entity;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;

public class ShowEntities
{
    private ShowEntities() { throw new UnsupportedOperationException(); }
    
    public static Optional<Component> asCustomName(Entity entity)
    {
        Objects.requireNonNull(entity, "entity");
        return Optional.ofNullable(entity.getCustomName())
            .map(LegacyComponentSerializer.legacySection()::deserialize);
    }
    
    public static Component asName(Entity entity)
    {
        return asCustomName(entity).orElseGet(() -> Component.text(entity.getName()));
    }
    
    public static HoverEvent<ShowEntity> asHover(Entity entity, @NullOr ComponentLike customName)
    {
        Objects.requireNonNull(entity, "entity");
        Key key = Key.key(entity.getType().getKey().toString(), ':');
        @NullOr Component name = (customName == null) ? null : customName.asComponent();
        ShowEntity showEntity = ShowEntity.of(key, entity.getUniqueId(), name);
        return HoverEvent.showEntity(showEntity);
    }
    
    public static HoverEvent<ShowEntity> asHover(Entity entity)
    {
        return asHover(entity, asCustomName(entity).orElse(null));
    }
}
