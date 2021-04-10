package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;

public interface LegacyBukkitTextChainSource extends BukkitAudiencesProvider, ChainSource<LegacyBukkitTextChain>
{
    @Override
    default ChainConstructor<LegacyBukkitTextChain> getChainConstructor()
    {
        return builder -> new LegacyBukkitTextChain(builder, adventure());
    }
}