package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.platforms.PlatformChainSource;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@FunctionalInterface
public interface BukkitTextChainSource extends PlatformChainSource<BukkitAudiences, BukkitTextChain>
{
    @Override
    default ChainConstructor<BukkitTextChain> getChainConstructor()
    {
        return builder -> new BukkitTextChain(builder, adventure());
    }
}
