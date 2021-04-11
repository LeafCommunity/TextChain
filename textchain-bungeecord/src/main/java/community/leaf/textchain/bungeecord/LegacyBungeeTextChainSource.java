package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import community.leaf.textchain.platforms.AdventureProvider;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

public interface LegacyBungeeTextChainSource extends AdventureProvider<BungeeAudiences>, ChainSource<LegacyBungeeTextChain>
{
    @Override
    default ChainConstructor<LegacyBungeeTextChain> getChainConstructor()
    {
        return builder -> new LegacyBungeeTextChain(builder, adventure());
    }
}
