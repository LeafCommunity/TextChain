package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;

import java.util.Optional;

public class BungeeToAdventure
{
    private BungeeToAdventure() { throw new UnsupportedOperationException(); }
    
    public static LegacyColorCodeAliases format(ChatColor color)
    {
        return LegacyColorCodeAliases.resolveByAlias(color.getName()).orElseThrow();
    }
    
    public static Optional<NamedTextColor> color(ChatColor color)
    {
        return format(color).asColor();
    }
    
    public static Optional<TextDecoration> decoration(ChatColor color)
    {
        return format(color).asDecoration();
    }
}
