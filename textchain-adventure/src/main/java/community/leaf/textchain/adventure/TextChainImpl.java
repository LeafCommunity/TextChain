package community.leaf.textchain.adventure;

final class TextChainImpl extends AbstractChain<TextChain> implements TextChain
{
    TextChainImpl(LinearTextComponentBuilder builder, TextProcessor processor)
    {
        super(builder, processor);
    }
    
    @Override
    public ChainConstructor<TextChain> constructor()
    {
        return TextChainImpl::new;
    }
}
