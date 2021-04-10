package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;

public interface BungeeTextChainSource extends ChainSource<BungeeTextChain>
{
    BungeeAudiences getAudiences();
    
    @Override
    default ChainConstructor<BungeeTextChain> getChainConstructor()
    {
        return builder -> new BungeeTextChain(builder, getAudiences());
    }
}
