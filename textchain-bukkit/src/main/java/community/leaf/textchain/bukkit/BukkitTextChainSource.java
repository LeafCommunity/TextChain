package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.adventure.TextChainSource;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public interface BukkitTextChainSource extends TextChainSource<BukkitTextChain>
{
    BukkitAudiences getAudiences();
    
    @Override
    default TextChainConstructor<BukkitTextChain> getTextChainConstructor()
    {
        return builder -> new BukkitTextChain(builder, getAudiences());
    }
}
