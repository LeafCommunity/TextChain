package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.RGBLike;

import java.util.Optional;

public enum ItemRarity implements ComponentLike, RGBLike
{
    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.YELLOW),
    RARE(NamedTextColor.AQUA),
    EPIC(NamedTextColor.LIGHT_PURPLE);
    
    private final NamedTextColor color;
    private final TextComponent component;
    
    ItemRarity(NamedTextColor color)
    {
        this.color = color;
        this.component = Component.text(name()).color(color);
    }
    
    public NamedTextColor getColor() { return color; }
    
    @Override
    public Component asComponent() { return component; }
    
    @Override
    public int red() { return color.red(); }
    
    @Override
    public int green() { return color.green(); }
    
    @Override
    public int blue() { return color.blue(); }
    
    public static Optional<ItemRarity> resolveByName(String name)
    {
        for (ItemRarity rarity : values())
        {
            if (rarity.name().equalsIgnoreCase(name)) { return Optional.of(rarity); }
        }
        return Optional.empty();
    }
}

