package community.leaf.textchain.adventure;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;

@FunctionalInterface
public interface ColorSource extends RGBLike
{
    TextColor color();
    
    @Override
    default int red() { return color().red(); }
    
    @Override
    default int green() { return color().green(); }
    
    @Override
    default int blue() { return color().blue(); }
}
