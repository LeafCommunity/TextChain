package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.platforms.PlatformChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

@FunctionalInterface
public interface LegacyBungeeTextChainSource extends PlatformChainSource<BungeeAudiences, LegacyBungeeTextChain>
{
    @Override
    default ChainConstructor<LegacyBungeeTextChain> getChainConstructor()
    {
        return builder -> new LegacyBungeeTextChain(builder, adventure());
    }
}
