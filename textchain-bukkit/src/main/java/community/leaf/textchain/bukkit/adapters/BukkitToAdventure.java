package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.platforms.adapters.ColorAdapter;
import community.leaf.textchain.platforms.adapters.EntityAdapter;
import community.leaf.textchain.platforms.adapters.ItemMetaAdapter;
import community.leaf.textchain.platforms.adapters.ItemTypeAdapter;
import community.leaf.textchain.platforms.adapters.KeyAdapter;
import community.leaf.textchain.platforms.adapters.delegates.AdventureEntity;
import community.leaf.textchain.platforms.adapters.delegates.AdventureItem;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BukkitToAdventure
{
    private BukkitToAdventure() { throw new UnsupportedOperationException(); }
    
    private static final BukkitColorAdapter colors = new BukkitColorAdapter();
    private static final BukkitKeyAdapter keys = new BukkitKeyAdapter();
    private static final BukkitEntityTypeAdapter entityTypes = new BukkitEntityTypeAdapter(keys);
    private static final BukkitEntityAdapter entities = new BukkitEntityAdapter(entityTypes);
    private static final BukkitMaterialAdapter materials = new BukkitMaterialAdapter(keys);
    private static final BukkitMetaAdapter meta = new BukkitMetaAdapter();
    private static final BukkitItemAdapter items = new BukkitItemAdapter(materials, meta);
    
    public static ColorAdapter<ChatColor> colors() { return colors; }
    
    public static KeyAdapter<NamespacedKey, Keyed> keys() { return keys; }
    
    public static EntityAdapter<EntityType, Entity> entities() { return entities; }
    
    public static AdventureEntity<Entity> entity(Entity entity) { return new AdventureEntity<>(entities, entity); }
    
    public static ItemTypeAdapter<Material> materials() { return materials; }
    
    public static ItemMetaAdapter<Material, ItemStack, ItemMeta> items() { return items; }
    
    public static AdventureItem<ItemStack> item(ItemStack item) { return new AdventureItem<>(items, item); }
}
