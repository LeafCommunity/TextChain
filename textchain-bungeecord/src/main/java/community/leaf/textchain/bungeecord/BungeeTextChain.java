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
    protected ChainConstructor<BungeeTextChain> getConstructor()
    {
        return builder -> new BungeeTextChain(builder, getAudiences());
    }
    
    @Override
    protected TextComponent processText(String text)
    {
        return TextProcessor.none(text);
    }
}
