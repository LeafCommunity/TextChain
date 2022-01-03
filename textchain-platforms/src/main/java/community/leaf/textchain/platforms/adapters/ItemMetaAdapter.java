/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;
import java.util.Optional;

public interface ItemMetaAdapter<T, I, M> extends ItemAdapter<T, I>
{
    MetaAdapter<M> meta();
    
    @SuppressWarnings("NullableProblems") // 404 no problem found
    @NullOr M meta(I item);
    
    void meta(I item, @NullOr M meta);
    
    @Override
    default Optional<Component> displayName(I item)
    {
        return meta().displayName(meta(item));
    }
    
    @Override
    default void displayName(I item, ComponentLike componentLike)
    {
        meta(item, meta().displayName(meta(item), componentLike));
    }
    
    @Override
    default List<Component> lore(I item)
    {
        return meta().lore(meta(item));
    }
    
    @Override
    default void lore(I item, List<Component> lore)
    {
         meta(item, meta().lore(meta(item), lore));
    }
    
    @Override
    default void lore(I item, ComponentLike componentLike)
    {
        meta(item, meta().lore(meta(item), componentLike));
    }
}
