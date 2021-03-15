package community.leaf.textchain.adventure;

public interface ChainSource<C extends Chain<C>>
{
    ChainConstructor<C> getChainConstructor();
}
