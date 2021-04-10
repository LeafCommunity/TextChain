package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.TextProcessor;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.TextComponent;

public class LegacyBungeeTextChain extends BungeeChain<LegacyBungeeTextChain>
{
    public LegacyBungeeTextChain(WrappedTextComponentBuilder builder, BungeeAudiences audiences)
    {
        super(builder, audiences);
    }
    
    @Override
    protected ChainConstructor<LegacyBungeeTextChain> getConstructor()
    {
        return builder -> new LegacyBungeeTextChain(builder, getAudiences());
    }
    
    @Override
    protected TextComponent processText(String text)
    {
        return TextProcessor.legacyAmpersand(text);
    }
}
