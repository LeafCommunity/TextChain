package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

import java.util.Optional;

public class BukkitToAdventure
{
    private BukkitToAdventure() { throw new UnsupportedOperationException(); }
    
    @SuppressWarnings("PatternValidation")
    public static Key key(NamespacedKey key)
    {
        return Key.key(key.getNamespace(), key.getKey());
    }
    
    public static Key key(Keyed keyed)
    {
        return key(keyed.getKey());
    }
    
    public static LegacyColorCodeAliases format(ChatColor color)
    {
        return LegacyColorCodeAliases.resolveByAlias(color.getName()).orElseThrow();
    }
    
    public static LegacyColorCodeAliases format(org.bukkit.ChatColor color)
    {
        return format(color.asBungee());
    }
    
    public static Optional<NamedTextColor> color(ChatColor color)
    {
        return format(color).asColor();
    }
    
    public static Optional<NamedTextColor> color(org.bukkit.ChatColor color)
    {
        return color(color.asBungee());
    }
    
    public static Optional<TextDecoration> decoration(ChatColor color)
    {
        return format(color).asDecoration();
    }
    
    public static Optional<TextDecoration> decoration(org.bukkit.ChatColor color)
    {
        return decoration(color.asBungee());
    }
}
