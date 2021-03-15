package community.leaf.textchain.adventure;

public final class TextComponentChain extends AbstractTextChain<TextComponentChain>
{
    public TextComponentChain(WrappedTextComponentBuilder builder) { super(builder); }
    
    @Override
    public TextChainConstructor<TextComponentChain> getConstructor() { return TextComponentChain::new; }
}
