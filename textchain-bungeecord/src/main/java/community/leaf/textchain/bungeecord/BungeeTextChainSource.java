package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;

public interface BungeeTextChainSource extends BungeeAudiencesProvider, ChainSource<BungeeTextChain>
{
    @Override
    default ChainConstructor<BungeeTextChain> getChainConstructor()
    {
        return builder -> new BungeeTextChain(builder, adventure());
    }
}
