package community.leaf.textchain.platforms.delegates;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.platforms.ItemConverter;
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
    private final ItemConverter<?, I> converter;
    private final I item;
    
    public AdventureItem(ItemConverter<?, I> converter, I item)
    {
        this.converter = Objects.requireNonNull(converter, "converter");
        this.item = Objects.requireNonNull(item, "item");
    }
    
    public I item() { return item; }
    
    @Override
    public Key key() { return converter.key(item); }
    
    public String translationKey() { return converter.translationKey(item); }
    
    public TranslatableComponent asTranslatable() { return converter.translatable(item); }
    
    public Optional<Component> displayName() { return converter.displayName(item); }
    
    public Component displayOrTranslatableName() { return converter.displayOrTranslatableName(item); }
    
    public void displayName(ComponentLike componentLike) { converter.displayName(item, componentLike); }
    
    public List<Component> lore() { return converter.lore(item); }
    
    public void lore(List<Component> lore) { converter.lore(item, lore); }
    
    public void lore(ComponentLike componentLike)
    {
        converter.lore(item, Components.flattenExtraSplitByNewLine(Components.safelyAsComponent(componentLike)));
    }
    
    public String clientName() { return converter.clientName(item); }
    
    public ItemRarity rarity() { return converter.rarity(item); }
    
    @Override
    public HoverEvent<ShowItem> asHoverEvent(UnaryOperator<ShowItem> op)
    {
        HoverEvent<ShowItem> hover = converter.hover(item);
        if (op == UnaryOperator.<ShowItem>identity()) { return hover; }
        return HoverEvent.showItem(op.apply(hover.value()));
    }
    
    @Override
    public Component asComponent() { return converter.component(item); }
    
    public Component asComponent(String prefix, String suffix) { return converter.component(item, prefix, suffix); }
    
    public Component asComponentInBrackets() { return converter.componentInBrackets(item); }
}
