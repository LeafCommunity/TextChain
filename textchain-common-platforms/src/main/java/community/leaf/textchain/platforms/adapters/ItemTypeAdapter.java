package community.leaf.textchain.platforms.adapters;

import community.leaf.textchain.adventure.ItemRarity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;

public interface ItemTypeAdapter<T>
{
    Key key(T type);
    
    String translationKey(T type);
    
    String clientName(T type);
    
    ItemRarity rarity(T type);
    
    default TranslatableComponent translatable(T type)
    {
        return Component.translatable(translationKey(type));
    }
}
