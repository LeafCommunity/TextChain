package community.leaf.textchain.bukkit.internal.nms;

import org.bukkit.entity.EntityType;

public interface EntityReflection
{
    static EntityReflection entities() { return EntityReflections.ENTITIES; }
    
    String translationKey(EntityType type) throws Throwable;
}
