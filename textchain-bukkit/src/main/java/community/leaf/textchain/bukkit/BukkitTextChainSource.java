package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public interface BukkitTextChainSource extends ChainSource<BukkitTextChain>
{
    BukkitAudiences getAudiences();
    
    @Override
    default ChainConstructor<BukkitTextChain> getChainConstructor()
    {
        return builder -> new BukkitTextChain(builder, getAudiences());
    }
}
