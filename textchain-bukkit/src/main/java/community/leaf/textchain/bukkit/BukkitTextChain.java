package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public final class BukkitTextChain extends BukkitChain<BukkitTextChain>
{
    public BukkitTextChain(WrappedTextComponentBuilder builder, BukkitAudiences audiences)
    {
        super(builder, audiences);
    }
    
    @Override
    public TextChainConstructor<BukkitTextChain> getConstructor()
    {
        return builder -> new BukkitTextChain(builder, getAudiences());
    }
}
