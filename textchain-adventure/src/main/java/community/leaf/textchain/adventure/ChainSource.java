package community.leaf.textchain.adventure;

/**
 * Generates standard chain constructors.
 *
 * @param <C>   chain type
 */
@FunctionalInterface
public interface ChainSource<C extends Chain<C>>
{
    /**
     * Generates a standard chain constructor by
     * supplying all other dependencies the specific
     * chain type requires.
     *
     * @return  a standard chain constructor
     */
    ChainConstructor<C> getChainConstructor();
}
