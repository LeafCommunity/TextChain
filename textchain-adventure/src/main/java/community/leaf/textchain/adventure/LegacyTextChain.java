package community.leaf.textchain.adventure;

import net.kyori.adventure.text.TextComponent;

public final class LegacyTextChain extends Chain<LegacyTextChain>
{
    public LegacyTextChain(WrappedTextComponentBuilder builder) { super(builder); }
    
    @Override
    protected ChainConstructor<LegacyTextChain> getConstructor() { return LegacyTextChain::new; }
    
    @Override
    protected TextComponent processText(String text) { return TextProcessor.legacyAmpersand(text); }
}
