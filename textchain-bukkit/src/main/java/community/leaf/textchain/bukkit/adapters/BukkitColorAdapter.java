package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import community.leaf.textchain.platforms.adapters.ColorAdapter;
import org.bukkit.ChatColor;

class BukkitColorAdapter implements ColorAdapter<ChatColor>
{
    @Override
    public LegacyColorCodeAliases format(ChatColor color)
    {
        return LegacyColorCodeAliases.resolveByCharacter(color.getChar()).orElseThrow();
    }
}
