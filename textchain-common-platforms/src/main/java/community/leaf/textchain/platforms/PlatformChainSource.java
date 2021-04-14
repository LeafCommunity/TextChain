package community.leaf.textchain.platforms;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainSource;
import net.kyori.adventure.platform.AudienceProvider;

public interface PlatformChainSource<A extends AudienceProvider, C extends Chain<C>>
    extends AdventureSource<A>, ChainSource<C> {}
