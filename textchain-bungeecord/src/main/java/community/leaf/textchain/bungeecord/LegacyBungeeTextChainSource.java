package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

public interface LegacyBungeeTextChainSource extends ChainSource<LegacyBungeeTextChain>
{
    BungeeAudiences getAudiences();
    
    @Override
    default ChainConstructor<LegacyBungeeTextChain> getChainConstructor()
    {
        return builder -> new LegacyBungeeTextChain(builder, getAudiences());
    }
}
