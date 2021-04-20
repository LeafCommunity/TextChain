package community.leaf.textchain.bukkit;

import community.leaf.textchain.bukkit.converters.BukkitColorConverter;
import community.leaf.textchain.bukkit.converters.BukkitEntityConverter;
import community.leaf.textchain.bukkit.converters.BukkitEntityTypeConverter;
import community.leaf.textchain.bukkit.converters.BukkitItemConverter;
import community.leaf.textchain.bukkit.converters.BukkitKeyConverter;
import community.leaf.textchain.bukkit.converters.BukkitMaterialConverter;
import community.leaf.textchain.platforms.delegates.AdventureEntity;
import community.leaf.textchain.platforms.delegates.AdventureItem;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class BukkitToAdventure
{
    private BukkitToAdventure() { throw new UnsupportedOperationException(); }
    
    private static final BukkitColorConverter colors = new BukkitColorConverter();
    private static final BukkitKeyConverter keys = new BukkitKeyConverter();
    private static final BukkitEntityTypeConverter entityTypes = new BukkitEntityTypeConverter(keys);
    private static final BukkitEntityConverter entities = new BukkitEntityConverter(entityTypes);
    private static final BukkitMaterialConverter materials = new BukkitMaterialConverter(keys);
    private static final BukkitItemConverter items = new BukkitItemConverter(materials);
    
    public static BukkitColorConverter colors() { return colors; }
    
    public static BukkitKeyConverter keys() { return keys; }
    
    public static BukkitEntityConverter entities() { return entities; }
    
    public static AdventureEntity<Entity> entity(Entity entity) { return new AdventureEntity<>(entities, entity); }
    
    public static BukkitMaterialConverter materials() { return materials; }
    
    public static BukkitItemConverter items() { return items; }
    
    public static AdventureItem<ItemStack> item(ItemStack item) { return new AdventureItem<>(items, item); }
}
