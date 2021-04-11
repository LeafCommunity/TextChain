package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import community.leaf.textchain.platforms.AdventureProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public interface BukkitTextChainSource extends AdventureProvider<BukkitAudiences>, ChainSource<BukkitTextChain>
{
    @Override
    default ChainConstructor<BukkitTextChain> getChainConstructor()
    {
        return builder -> new BukkitTextChain(builder, adventure());
    }
}
