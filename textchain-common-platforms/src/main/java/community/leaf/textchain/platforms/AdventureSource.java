package community.leaf.textchain.platforms;

import net.kyori.adventure.platform.AudienceProvider;

public interface AdventureSource<A extends AudienceProvider>
{
    A adventure();
}
