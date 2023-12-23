/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters.delegates;

import community.leaf.textchain.platforms.ItemRarity;
import community.leaf.textchain.platforms.adapters.ItemAdapter;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent.ShowItem;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.translation.Translatable;

import java.util.List;
import java.util.Optional;

public interface AdventureItem<I> extends ComponentLike, HoverEventSource<ShowItem>, Keyed, Translatable
{
    static <T, I> AdventureItem<I> of(ItemAdapter<T, I> adapter, I item)
    {
        return new AdventureItemImpl<>(adapter, item);
    }
    
    I item();
    
    String translationKey();
    
    TranslatableComponent asTranslatable();
    
    Optional<Component> displayName();
    
    Component displayOrTranslatableName();
    
    void displayName(ComponentLike componentLike);
    
    List<Component> lore();
    
    void lore(List<Component> lore);
    
    void lore(ComponentLike componentLike);
    
    String clientName();
    
    ItemRarity rarity();
    
    Component asComponent(String prefix, String suffix);
    
    Component asComponentInBrackets();
}
