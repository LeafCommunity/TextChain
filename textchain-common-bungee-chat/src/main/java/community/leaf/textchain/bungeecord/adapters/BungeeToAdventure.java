package community.leaf.textchain.bungeecord.adapters;

import community.leaf.textchain.platforms.adapters.ColorAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

public class BungeeToAdventure
{
    private BungeeToAdventure() { throw new UnsupportedOperationException(); }
    
    private static final BungeeColorAdapter colors = new BungeeColorAdapter();
    
    public static ColorAdapter<ChatColor> colors() { return colors; }
    
    public static Component component(BaseComponent[] baseComponents)
    {
        return BungeeComponentSerializer.get().deserialize(baseComponents);
    }
}
