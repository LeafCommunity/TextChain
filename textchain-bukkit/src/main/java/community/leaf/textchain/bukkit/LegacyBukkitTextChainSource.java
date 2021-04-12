package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.ChainSource;
import community.leaf.textchain.platforms.AdventureProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@FunctionalInterface
public interface LegacyBukkitTextChainSource extends AdventureProvider<BukkitAudiences>, ChainSource<LegacyBukkitTextChain>
{
    @Override
    default ChainConstructor<LegacyBukkitTextChain> getChainConstructor()
    {
        return builder -> new LegacyBukkitTextChain(builder, adventure());
    }
}