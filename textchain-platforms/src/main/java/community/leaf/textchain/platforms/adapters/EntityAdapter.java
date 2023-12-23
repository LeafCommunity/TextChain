/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowEntity;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Optional;
import java.util.UUID;

public interface EntityAdapter<T, E>
{
    EntityTypeAdapter<T> types();
    
    T type(E entity);
    
    UUID uuid(E entity);
    
    Optional<Component> customName(E entity);
    
    void customName(E entity, ComponentLike componentLike);
    
    default Key key(E entity)
    {
        return types().key(type(entity));
    }
    
    default HoverEvent<ShowEntity> hover(E entity, @NullOr ComponentLike customName)
    {
        @NullOr Component name = (customName == null) ? null : Components.safelyAsComponent(customName);
        return HoverEvent.showEntity(ShowEntity.of(key(entity), uuid(entity), name));
    }
    
    default HoverEvent<ShowEntity> hover(E entity)
    {
        return hover(entity, customName(entity).orElse(null));
    }
    
    default String translationKey(E entity)
    {
        return types().translationKey(type(entity));
    }
    
    default TranslatableComponent translatable(E entity)
    {
        return types().translatable(type(entity));
    }
    
    default Component customOrTranslatableName(E entity)
    {
        return customName(entity).orElseGet(() -> translatable(entity));
    }
    
    default TextComponent component(E entity, String prefix, String suffix)
    {
        return TextChain.chain()
            .extra(chain -> {
                if (!prefix.isEmpty()) { chain.then(prefix); }
                chain.then(customOrTranslatableName(entity));
                if (!suffix.isEmpty()) { chain.then(suffix); }
            })
            .hover(hover(entity))
            .asComponent();
    }
    
    default TextComponent component(E entity) { return component(entity, "", ""); }
    
    default TextComponent componentInBrackets(E entity) { return component(entity, "[", "]"); }
}
