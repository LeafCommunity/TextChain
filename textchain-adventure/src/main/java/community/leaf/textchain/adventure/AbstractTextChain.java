package community.leaf.textchain.adventure;

import java.util.Objects;

public abstract class AbstractTextChain<C extends AbstractTextChain<C>> implements TextChain<C>
{
    private final WrappedTextComponentBuilder builder;
    
    public AbstractTextChain(WrappedTextComponentBuilder builder)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
    }
    
    @Override
    public final WrappedTextComponentBuilder getBuilder() { return builder; }
}
