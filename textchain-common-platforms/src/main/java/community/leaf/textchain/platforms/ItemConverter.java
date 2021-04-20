package community.leaf.textchain.platforms;

import community.leaf.textchain.adventure.Components;
import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.List;
import java.util.Optional;

public interface ItemConverter<T, I>
{
    ItemTypeConverter<T> types();
    
    T type(I item);
    
    int amount(I item);
    
    BinaryTagHolder nbt(I item);
    
    Optional<Component> displayName(I item);
    
    void displayName(I item, ComponentLike componentLike);
    
    List<Component> lore(I item);
    
    void lore(I item, List<Component> lore);
    
    ItemRarity rarity(I item);
    
    default Key key(I item)
    {
        return types().key(type(item));
    }
    
    default HoverEvent<HoverEvent.ShowItem> hover(I item)
    {
        return HoverEvent.showItem(HoverEvent.ShowItem.of(key(item), amount(item), nbt(item)));
    }
    
    default String translationKey(I item)
    {
        return types().translationKey(type(item));
    }
    
    default TranslatableComponent translatable(I item)
    {
        return types().translatable(type(item));
    }
    
    default Component displayOrTranslatableName(I item)
    {
        return displayName(item).orElseGet(() -> translatable(item));
    }
    
    default void lore(I item, ComponentLike componentLike)
    {
        lore(item, Components.flattenExtraSplitByNewLine(Components.safelyAsComponent(componentLike)));
    }
    
    default String clientName(I item)
    {
        return types().clientName(type(item));
    }
    
    default TextComponent component(I item, String prefix, String suffix)
    {
        return TextChain.chain()
            .extra(chain -> {
                if (!prefix.isEmpty()) { chain.then(prefix); }
                chain.then(displayOrTranslatableName(item));
                if (!suffix.isEmpty()) { chain.then(suffix); }
            })
            .hover(hover(item))
            .asComponent();
    }
    
    default TextComponent component(I item) { return component(item, "", ""); }
    
    default TextComponent componentInBrackets(I item) { return component(item, "[", "]"); }
}
