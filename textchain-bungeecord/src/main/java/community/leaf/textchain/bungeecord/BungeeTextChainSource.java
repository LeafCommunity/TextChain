package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import community.leaf.textchain.platforms.AdventureProvider;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

public interface BungeeTextChainSource extends AdventureProvider<BungeeAudiences>, ChainSource<BungeeTextChain>
{
    @Override
    default ChainConstructor<BungeeTextChain> getChainConstructor()
    {
        return builder -> new BungeeTextChain(builder, adventure());
    }
}
