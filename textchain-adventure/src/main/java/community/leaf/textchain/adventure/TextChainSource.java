package community.leaf.textchain.adventure;

public interface TextChainSource<C extends TextChain<C>>
{
    TextChainConstructor<C> getTextChainConstructor();
}
