package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import community.leaf.textchain.bukkit.converters.BukkitEntityConverter;
import community.leaf.textchain.bukkit.converters.NamespacedKeyConverter;
import community.leaf.textchain.platforms.ColorConverter;
import community.leaf.textchain.platforms.delegates.AdventureEntity;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

public class BukkitToAdventure
{
    private BukkitToAdventure() { throw new UnsupportedOperationException(); }
    
    private static final NamespacedKeyConverter keys = new NamespacedKeyConverter();
    private static final BukkitEntityConverter entities = new BukkitEntityConverter(keys);
    
    public static ColorConverter<ChatColor> colors()
    {
        return color -> LegacyColorCodeAliases.resolveByCharacter(color.getChar()).orElseThrow();
    }
    
    public static NamespacedKeyConverter keys() { return keys; }
    
    public static BukkitEntityConverter entities() { return entities; }
    
    public static AdventureEntity<Entity> entity(Entity entity) { return new AdventureEntity<>(entities, entity); }
}
