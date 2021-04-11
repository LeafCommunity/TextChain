package community.leaf.textchain.platforms;

import net.kyori.adventure.platform.AudienceProvider;

public interface AdventureProvider<A extends AudienceProvider>
{
    A adventure();
}
