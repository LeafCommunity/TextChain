package community.leaf.textchain.bukkit.converters;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import community.leaf.textchain.platforms.ColorConverter;
import org.bukkit.ChatColor;

public class BukkitColorConverter implements ColorConverter<ChatColor>
{
    @Override
    public LegacyColorCodeAliases format(ChatColor color)
    {
        return LegacyColorCodeAliases.resolveByCharacter(color.getChar()).orElseThrow();
    }
}
