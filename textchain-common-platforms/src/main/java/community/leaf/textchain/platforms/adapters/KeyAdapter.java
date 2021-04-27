package community.leaf.textchain.platforms.adapters;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

public interface KeyAdapter<K, H>
{
    Key key(K key);
    
    Keyed keyed(H keyed);
}
