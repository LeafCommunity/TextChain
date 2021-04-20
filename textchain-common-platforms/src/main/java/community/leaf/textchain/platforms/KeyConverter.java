package community.leaf.textchain.platforms;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.function.Supplier;

public interface KeyConverter<K, H>
{
    Key key(K key);
    
    Keyed keyed(H keyed);
}
