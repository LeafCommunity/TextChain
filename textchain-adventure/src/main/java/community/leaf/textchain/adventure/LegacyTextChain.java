package community.leaf.textchain.adventure;

import net.kyori.adventure.text.TextComponent;

/**
 * A text chain that will automatically process legacy
 * ampersand-style color codes in text (like {@code "&3&o"})
 * by using {@link TextProcessor#legacyAmpersand(String)}.
 * To append text without parsing color codes, simply use
 * {@link #thenUnprocessed(String)}.
 */
public final class LegacyTextChain extends Chain<LegacyTextChain>
{
    public LegacyTextChain(WrappedTextComponentBuilder builder) { super(builder); }
    
    @Override
    protected ChainConstructor<LegacyTextChain> getConstructor() { return LegacyTextChain::new; }
    
    @Override
    protected TextComponent processText(String text) { return TextProcessor.legacyAmpersand(text); }
}
