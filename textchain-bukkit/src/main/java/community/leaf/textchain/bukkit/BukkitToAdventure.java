package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
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
        return LegacyColorCodeAliases.resolveByCharacter(color.getChar()).orElseThrow();
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
