package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;

public interface BukkitTextChainSource extends BukkitAudiencesProvider, ChainSource<BukkitTextChain>
{
    @Override
    default ChainConstructor<BukkitTextChain> getChainConstructor()
    {
        return builder -> new BukkitTextChain(builder, adventure());
    }
}
