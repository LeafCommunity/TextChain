package community.leaf.textchain.bungeecord.chat;

import community.leaf.textchain.platforms.ColorConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

public class BungeeToAdventure
{
    private BungeeToAdventure() { throw new UnsupportedOperationException(); }
    
    private static final BungeeColorConverter colors = new BungeeColorConverter();
    
    public static ColorConverter<ChatColor> colors() { return colors; }
    
    public static Component component(BaseComponent[] baseComponents)
    {
        return BungeeComponentSerializer.get().deserialize(baseComponents);
    }
}
