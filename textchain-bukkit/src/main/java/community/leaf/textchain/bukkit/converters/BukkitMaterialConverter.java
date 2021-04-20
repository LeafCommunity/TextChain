package community.leaf.textchain.bukkit.converters;

import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.platforms.ItemTypeConverter;
import net.kyori.adventure.key.Key;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.Material;

import static community.leaf.textchain.bukkit.internal.nms.ItemReflection.*;

public class BukkitMaterialConverter implements ItemTypeConverter<Material>
{
    private final BukkitKeyConverter keys;
    
    public BukkitMaterialConverter(BukkitKeyConverter keys) { this.keys = keys; }
    
    @Override
    public Key key(Material type)
    {
        return keys.key(type.getKey());
    }
    
    @Override
    public String translationKey(Material type)
    {
        try
        {
            if (type.isBlock()) { return getNmsBlockName(getNmsBlockByMaterial(type)); }
            else { return getNmsItemName(getNmsItemByMaterial(type)); }
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
    
    @Override
    public String clientName(Material type)
    {
        return TranslationRegistry.INSTANCE.translate(translationKey(type));
    }
    
    @Override
    public ItemRarity rarity(Material type)
    {
        if (!type.isItem()) { return ItemRarity.COMMON; }
        
        try
        {
            Object nmsItem = getNmsItemByMaterial(type);
            Object nmsRarity = getItemRarityOfNmsItem(nmsItem);
            return ItemRarity.resolveByName(String.valueOf(nmsRarity)).orElse(ItemRarity.COMMON);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
}
