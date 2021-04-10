package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.TextProcessor;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.TextComponent;

public final class BukkitTextChain extends BukkitChain<BukkitTextChain>
{
    public BukkitTextChain(WrappedTextComponentBuilder builder, BukkitAudiences audiences)
    {
        super(builder, audiences);
    }
    
    @Override
    public ChainConstructor<BukkitTextChain> getConstructor()
    {
        return builder -> new BukkitTextChain(builder, adventure());
    }
    
    @Override
    public TextComponent processText(String text)
    {
        return TextProcessor.none(text);
    }
}
