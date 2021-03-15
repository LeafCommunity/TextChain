package community.leaf.textchain.adventure;

public interface TextChainSource<C extends Chain<C>>
{
    TextChainConstructor<C> getTextChainConstructor();
}
