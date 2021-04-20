package community.leaf.textchain.bungeecord.chat;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import community.leaf.textchain.platforms.ColorConverter;
import net.md_5.bungee.api.ChatColor;

public class BungeeColorConverter implements ColorConverter<ChatColor>
{
    @Override
    public LegacyColorCodeAliases format(ChatColor color)
    {
        return LegacyColorCodeAliases.resolveByAlias(color.getName()).orElseThrow();
    }
}
