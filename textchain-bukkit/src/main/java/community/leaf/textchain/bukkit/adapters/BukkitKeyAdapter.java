package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.platforms.adapters.KeyAdapter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;

class BukkitKeyAdapter implements KeyAdapter<NamespacedKey, org.bukkit.Keyed>
{
    @SuppressWarnings("PatternValidation")
    @Override
    public Key key(NamespacedKey key)
    {
        return Key.key(key.getNamespace(), key.getKey());
    }
    
    @Override
    public Keyed keyed(org.bukkit.Keyed keyed)
    {
        Key converted = key(keyed.getKey());
        return () -> converted;
    }
}
