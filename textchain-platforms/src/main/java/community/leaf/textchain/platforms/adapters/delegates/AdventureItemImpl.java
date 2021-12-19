/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters.delegates;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.platforms.ItemRarity;
import community.leaf.textchain.platforms.adapters.ItemAdapter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

class AdventureItemImpl<I> implements AdventureItem<I>
{
    private final ItemAdapter<?, I> adapter;
    private final I item;
    
    AdventureItemImpl(ItemAdapter<?, I> adapter, I item)
    {
        this.adapter = Objects.requireNonNull(adapter, "adapter");
        this.item = Objects.requireNonNull(item, "item");
    }
    
    @SuppressWarnings("NullableProblems")
    @Override
    public I item() { return item; }
    
    @Override
    public Key key() { return adapter.key(item); }
    
    @Override
    public String translationKey() { return adapter.translationKey(item); }
    
    @Override
    public TranslatableComponent asTranslatable() { return adapter.translatable(item); }
    
    @Override
    public Optional<Component> displayName() { return adapter.displayName(item); }
    
    @Override
    public Component displayOrTranslatableName() { return adapter.displayOrTranslatableName(item); }
    
    @Override
    public void displayName(ComponentLike componentLike) { adapter.displayName(item, componentLike); }
    
    @Override
    public List<Component> lore() { return adapter.lore(item); }
    
    @Override
    public void lore(List<Component> lore) { adapter.lore(item, lore); }
    
    @Override
    public void lore(ComponentLike componentLike)
    {
        adapter.lore(item, Components.flattenExtraSplitByNewLine(Components.safelyAsComponent(componentLike)));
    }
    
    @Override
    public String clientName() { return adapter.clientName(item); }
    
    @Override
    public ItemRarity rarity() { return adapter.rarity(item); }
    
    @Override
    public HoverEvent<HoverEvent.ShowItem> asHoverEvent(UnaryOperator<HoverEvent.ShowItem> op)
    {
        HoverEvent<HoverEvent.ShowItem> hover = adapter.hover(item);
        if (op == UnaryOperator.<HoverEvent.ShowItem>identity()) { return hover; }
        return HoverEvent.showItem(op.apply(hover.value()));
    }
    
    @Override
    public Component asComponent() { return adapter.component(item); }
    
    @Override
    public Component asComponent(String prefix, String suffix) { return adapter.component(item, prefix, suffix); }
    
    @Override
    public Component asComponentInBrackets() { return adapter.componentInBrackets(item); }
}
