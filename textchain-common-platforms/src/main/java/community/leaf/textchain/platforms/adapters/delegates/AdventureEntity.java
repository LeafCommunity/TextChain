package community.leaf.textchain.platforms.adapters.delegates;

import community.leaf.textchain.platforms.adapters.EntityAdapter;
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
    private final EntityAdapter<?, E> adapter;
    private final E entity;
    
    public AdventureEntity(EntityAdapter<?, E> adapter, E entity)
    {
        this.adapter = Objects.requireNonNull(adapter, "adapter");
        this.entity = Objects.requireNonNull(entity, "entity");
    }
    
    public E entity() { return entity; }
    
    public UUID uuid() { return adapter.uuid(entity); }
    
    @Override
    public Key key() { return adapter.key(entity); }
    
    public String translationKey() { return adapter.translationKey(entity); }
    
    public TranslatableComponent asTranslatable() { return adapter.translatable(entity); }
    
    public Optional<Component> customName() { return adapter.customName(entity); }
    
    public void customName(ComponentLike componentLike) { adapter.customName(entity, componentLike); }
    
    public Component customOrTranslatableName() { return adapter.customOrTranslatableName(entity); }
    
    public HoverEvent<ShowEntity> asHoverEvent(@NullOr ComponentLike customName)
    {
        return adapter.hover(entity, customName);
    }
    
    @Override // Emulates the asHoverEvent() implementation found in HoverEvent
    public HoverEvent<ShowEntity> asHoverEvent(UnaryOperator<ShowEntity> op)
    {
        HoverEvent<ShowEntity> hover = adapter.hover(entity);
        if (op == UnaryOperator.<ShowEntity>identity()) { return hover; }
        return HoverEvent.showEntity(op.apply(hover.value()));
    }
    
    @Override
    public Component asComponent() { return adapter.component(entity); }
    
    public Component asComponent(String prefix, String suffix) { return adapter.component(entity, prefix, suffix); }
    
    public Component asComponentInBrackets() { return adapter.componentInBrackets(entity); }
}
