package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;

public interface LegacyBungeeTextChainSource extends BungeeAudiencesProvider, ChainSource<LegacyBungeeTextChain>
{
    @Override
    default ChainConstructor<LegacyBungeeTextChain> getChainConstructor()
    {
        return builder -> new LegacyBungeeTextChain(builder, adventure());
    }
}
