package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.platforms.PlatformChainSource;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@FunctionalInterface
public interface LegacyBukkitTextChainSource extends PlatformChainSource<BukkitAudiences, LegacyBukkitTextChain>
{
    @Override
    default ChainConstructor<LegacyBukkitTextChain> getChainConstructor()
    {
        return builder -> new LegacyBukkitTextChain(builder, adventure());
    }
}