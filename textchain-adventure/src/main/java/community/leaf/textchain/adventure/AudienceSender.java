package community.leaf.textchain.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;

public interface AudienceSender<S extends AudienceSender<S>>
{
    S send(Audience audience);
    
    S send(Audience audience, Identity source);
    
    S send(Audience audience, Identified source);
    
    S actionBar(Audience audience);
}
