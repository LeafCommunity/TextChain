package community.leaf.textchain.bukkit;

import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public class BukkitKeys
{
    private BukkitKeys() { throw new UnsupportedOperationException(); }
    
    @SuppressWarnings("PatternValidation")
    public static Key convertKey(NamespacedKey key)
    {
        return Key.key(key.getNamespace(), key.getKey());
    }
    
    public static Key convertKey(Keyed keyed)
    {
        return convertKey(keyed.getKey());
    }
}
