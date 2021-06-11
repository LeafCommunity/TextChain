package community.leaf.textchain.bukkit.internal.nms;

import community.leaf.textchain.adventure.ItemRarity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface ItemReflection
{
    static ItemReflection items() { return ItemReflections.ITEMS; }
    
    String translationKey(Material type) throws Throwable;
    
    String toCompoundTag(ItemStack item) throws Throwable;
    
    ItemRarity rarity(Material type) throws Throwable;
    
    ItemRarity rarity(ItemStack item) throws Throwable;
}
