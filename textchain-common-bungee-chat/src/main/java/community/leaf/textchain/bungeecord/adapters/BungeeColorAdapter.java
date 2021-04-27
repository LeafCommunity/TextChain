package community.leaf.textchain.bungeecord.adapters;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import community.leaf.textchain.platforms.adapters.ColorAdapter;
import net.md_5.bungee.api.ChatColor;

class BungeeColorAdapter implements ColorAdapter<ChatColor>
{
    @Override
    public LegacyColorCodeAliases format(ChatColor color)
    {
        return LegacyColorCodeAliases.resolveByAlias(color.getName()).orElseThrow();
    }
}
