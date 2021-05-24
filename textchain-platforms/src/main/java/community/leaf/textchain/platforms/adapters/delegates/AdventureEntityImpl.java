/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters.delegates;

import community.leaf.textchain.platforms.adapters.EntityAdapter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

class AdventureEntityImpl<E> implements AdventureEntity<E>
{
    private final EntityAdapter<?, E> adapter;
    private final E entity;
    
    AdventureEntityImpl(EntityAdapter<?, E> adapter, E entity)
    {
        this.adapter = Objects.requireNonNull(adapter, "adapter");
        this.entity = Objects.requireNonNull(entity, "entity");
    }
    
    @SuppressWarnings("NullableProblems") // E
    @Override
    public E entity() { return entity; }
    
    @Override
    public UUID uuid() { return adapter.uuid(entity); }
    
    @Override
    public Key key() { return adapter.key(entity); }
    
    @Override
    public String translationKey() { return adapter.translationKey(entity); }
    
    @Override
    public TranslatableComponent asTranslatable() { return adapter.translatable(entity); }
    
    @Override
    public Optional<Component> customName() { return adapter.customName(entity); }
    
    @Override
    public void customName(ComponentLike componentLike) { adapter.customName(entity, componentLike); }
    
    @Override
    public Component customOrTranslatableName() { return adapter.customOrTranslatableName(entity); }
    
    @Override
    public HoverEvent<HoverEvent.ShowEntity> asHoverEvent(@NullOr ComponentLike customName)
    {
        return adapter.hover(entity, customName);
    }
    
    @Override // Emulates the asHoverEvent() implementation found in HoverEvent
    public HoverEvent<HoverEvent.ShowEntity> asHoverEvent(UnaryOperator<HoverEvent.ShowEntity> op)
    {
        HoverEvent<HoverEvent.ShowEntity> hover = adapter.hover(entity);
        if (op == UnaryOperator.<HoverEvent.ShowEntity>identity()) { return hover; }
        return HoverEvent.showEntity(op.apply(hover.value()));
    }
    
    @Override
    public Component asComponent() { return adapter.component(entity); }
    
    @Override
    public Component asComponent(String prefix, String suffix) { return adapter.component(entity, prefix, suffix); }
    
    @Override
    public Component asComponentInBrackets() { return adapter.componentInBrackets(entity); }
}
