package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public interface LegacyBukkitTextChainSource extends ChainSource<LegacyBukkitTextChain>
{
    BukkitAudiences getAudiences();
    
    @Override
    default ChainConstructor<LegacyBukkitTextChain> getChainConstructor()
    {
        return builder -> new LegacyBukkitTextChain(builder, getAudiences());
    }
}