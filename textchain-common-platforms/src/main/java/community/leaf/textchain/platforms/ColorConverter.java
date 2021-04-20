package community.leaf.textchain.platforms;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Optional;

@FunctionalInterface
public interface ColorConverter<C>
{
    LegacyColorCodeAliases format(C color);
    
    default Optional<NamedTextColor> color(C color)
    {
        return format(color).asColor();
    }
    
    default Optional<TextDecoration> decoration(C color)
    {
        return format(color).asDecoration();
    }
}
