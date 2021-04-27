package community.leaf.textchain.platforms.adapters;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;

public interface EntityTypeAdapter<T>
{
    Key key(T type);
    
    String translationKey(T type);
    
    default TranslatableComponent translatable(T type)
    {
        return Component.translatable(translationKey(type));
    }
}
