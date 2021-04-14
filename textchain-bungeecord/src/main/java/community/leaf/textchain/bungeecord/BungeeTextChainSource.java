package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.platforms.PlatformChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

@FunctionalInterface
public interface BungeeTextChainSource extends PlatformChainSource<BungeeAudiences, BungeeTextChain>
{
    @Override
    default ChainConstructor<BungeeTextChain> getChainConstructor()
    {
        return builder -> new BungeeTextChain(builder, adventure());
    }
}
