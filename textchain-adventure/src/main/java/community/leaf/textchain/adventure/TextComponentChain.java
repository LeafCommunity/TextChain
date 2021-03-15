package community.leaf.textchain.adventure;

import java.util.Objects;

public class TextComponentChain implements TextChain<TextComponentChain>
{
    private final WrappedTextComponentBuilder builder;
    
    public TextComponentChain(WrappedTextComponentBuilder builder)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
    }
    
    @Override
    public WrappedTextComponentBuilder getBuilder() { return builder; }

    @Override
    public TextChainConstructor<TextComponentChain> getConstructor() { return TextComponentChain::new; }
}
