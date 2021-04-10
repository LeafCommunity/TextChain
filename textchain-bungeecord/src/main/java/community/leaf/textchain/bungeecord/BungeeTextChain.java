package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.TextProcessor;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.TextComponent;

public final class BungeeTextChain extends BungeeChain<BungeeTextChain>
{
    public BungeeTextChain(WrappedTextComponentBuilder builder, BungeeAudiences audiences)
    {
        super(builder, audiences);
    }
    
    @Override
    public ChainConstructor<BungeeTextChain> getConstructor()
    {
        return builder -> new BungeeTextChain(builder, adventure());
    }
    
    @Override
    public TextComponent processText(String text)
    {
        return TextProcessor.none(text);
    }
}
