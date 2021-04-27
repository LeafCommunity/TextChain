package community.leaf.textchain.bukkit.adapters;

import community.leaf.textchain.platforms.adapters.EntityTypeAdapter;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;
import pl.tlinkowski.annotation.basic.NullOr;

import static community.leaf.textchain.bukkit.internal.nms.EntityReflection.*;

class BukkitEntityTypeAdapter implements EntityTypeAdapter<EntityType>
{
    private final BukkitKeyAdapter keys;
    
    public BukkitEntityTypeAdapter(BukkitKeyAdapter keys) { this.keys = keys; }
    
    @Override
    public Key key(EntityType type)
    {
        return keys.key(type.getKey());
    }
    
    @Override
    public String translationKey(EntityType type)
    {
        @NullOr String entityTypeName = type.getName();
        
        try
        {
            Object nmsEntityTypes = getEntityTypesByName(entityTypeName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid entity name: " + entityTypeName));
            
            return getTranslationKeyByNmsEntityTypes(nmsEntityTypes);
        }
        catch (Throwable throwable) { throw new RuntimeException(throwable); }
    }
}
