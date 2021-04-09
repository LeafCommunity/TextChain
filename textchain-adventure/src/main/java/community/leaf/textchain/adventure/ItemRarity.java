package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.RGBLike;

import java.util.Optional;

/**
 * Represents an item's rarity.
 * This enum is a one-to-one reimplementation of Minecraft's
 * internal {@code net.minecraft.server.EnumItemRarity},
 * but it uses Adventure colors, components, and isn't
 * restricted to a specific platform.
 */
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
    
    /**
     * Gets the color associated with this
     * level of rarity.
     *
     * @return  rarity level's color
     */
    public NamedTextColor getColor() { return color; }
    
    @Override
    public Component asComponent() { return component; }
    
    @Override
    public int red() { return color.red(); }
    
    @Override
    public int green() { return color.green(); }
    
    @Override
    public int blue() { return color.blue(); }
    
    /**
     * Resolves the enum value that matches the
     * case-insensitive input, or else empty.
     *
     * @param name  possible name of an enum value
     * @return  the resolved enum value or empty
     */
    public static Optional<ItemRarity> resolveByName(String name)
    {
        for (ItemRarity rarity : values())
        {
            if (rarity.name().equalsIgnoreCase(name)) { return Optional.of(rarity); }
        }
        return Optional.empty();
    }
}
