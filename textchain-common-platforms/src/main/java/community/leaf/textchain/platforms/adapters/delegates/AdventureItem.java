/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters.delegates;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.platforms.adapters.ItemAdapter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEvent.ShowItem;
import net.kyori.adventure.text.event.HoverEventSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class AdventureItem<I> implements ComponentLike, HoverEventSource<ShowItem>, Keyed
{
    private final ItemAdapter<?, I> adapter;
    private final I item;
    
    public AdventureItem(ItemAdapter<?, I> adapter, I item)
    {
        this.adapter = Objects.requireNonNull(adapter, "adapter");
        this.item = Objects.requireNonNull(item, "item");
    }
    
    public I item() { return item; }
    
    @Override
    public Key key() { return adapter.key(item); }
    
    public String translationKey() { return adapter.translationKey(item); }
    
    public TranslatableComponent asTranslatable() { return adapter.translatable(item); }
    
    public Optional<Component> displayName() { return adapter.displayName(item); }
    
    public Component displayOrTranslatableName() { return adapter.displayOrTranslatableName(item); }
    
    public void displayName(ComponentLike componentLike) { adapter.displayName(item, componentLike); }
    
    public List<Component> lore() { return adapter.lore(item); }
    
    public void lore(List<Component> lore) { adapter.lore(item, lore); }
    
    public void lore(ComponentLike componentLike)
    {
        adapter.lore(item, Components.flattenExtraSplitByNewLine(Components.safelyAsComponent(componentLike)));
    }
    
    public String clientName() { return adapter.clientName(item); }
    
    public ItemRarity rarity() { return adapter.rarity(item); }
    
    @Override
    public HoverEvent<ShowItem> asHoverEvent(UnaryOperator<ShowItem> op)
    {
        HoverEvent<ShowItem> hover = adapter.hover(item);
        if (op == UnaryOperator.<ShowItem>identity()) { return hover; }
        return HoverEvent.showItem(op.apply(hover.value()));
    }
    
    @Override
    public Component asComponent() { return adapter.component(item); }
    
    public Component asComponent(String prefix, String suffix) { return adapter.component(item, prefix, suffix); }
    
    public Component asComponentInBrackets() { return adapter.componentInBrackets(item); }
}
