package community.leaf.textchain.platforms.delegates;

import community.leaf.textchain.platforms.EntityConverter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowEntity;
import net.kyori.adventure.text.event.HoverEventSource;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class AdventureEntity<E> implements ComponentLike, HoverEventSource<ShowEntity>, Keyed
{
    private final EntityConverter<?, E> converter;
    private final E entity;
    
    public AdventureEntity(EntityConverter<?, E> converter, E entity)
    {
        this.converter = Objects.requireNonNull(converter, "converter");
        this.entity = Objects.requireNonNull(entity, "entity");
    }
    
    public E entity() { return entity; }
    
    public UUID uuid() { return converter.uuid(entity); }
    
    @Override
    public Key key() { return converter.entityKey(entity); }
    
    public String translationKey() { return converter.translationKey(entity); }
    
    public TranslatableComponent asTranslatable() { return converter.translatable(entity); }
    
    public Optional<Component> customName() { return converter.customName(entity); }
    
    public void customName(ComponentLike componentLike) { converter.customName(entity, componentLike); }
    
    public Component customOrTranslatableName() { return converter.customOrTranslatableName(entity); }
    
    public HoverEvent<ShowEntity> asHoverEvent(@NullOr ComponentLike customName)
    {
        return converter.hover(entity, customName);
    }
    
    @Override // Emulates the asHoverEvent() implementation found in HoverEvent
    public HoverEvent<ShowEntity> asHoverEvent(UnaryOperator<ShowEntity> op)
    {
        HoverEvent<ShowEntity> hover = converter.hover(entity);
        if (op == UnaryOperator.<ShowEntity>identity()) { return hover; }
        return HoverEvent.showEntity(op.apply(hover.value()));
    }
    
    @Override
    public Component asComponent() { return converter.component(entity); }
    
    public Component asComponent(String prefix, String suffix) { return converter.component(entity, prefix, suffix); }
    
    public Component asComponentInBrackets() { return converter.componentInBrackets(entity); }
}
