/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters.delegates;

import community.leaf.textchain.platforms.adapters.EntityAdapter;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowEntity;
import net.kyori.adventure.text.event.HoverEventSource;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Optional;
import java.util.UUID;

public interface AdventureEntity<E> extends ComponentLike, HoverEventSource<ShowEntity>, Keyed
{
    static <T, E> AdventureEntity<E> of(EntityAdapter<T, E> adapter, E entity)
    {
        return new AdventureEntityImpl<>(adapter, entity);
    }
    
    E entity();

    UUID uuid();
    
    String translationKey();
    
    TranslatableComponent asTranslatable();
    
    Optional<Component> customName();
    
    void customName(ComponentLike componentLike);
    
    Component customOrTranslatableName();
    
    HoverEvent<HoverEvent.ShowEntity> asHoverEvent(@NullOr ComponentLike customName);
    
    Component asComponent(String prefix, String suffix);
    
    Component asComponentInBrackets();
}
