package community.leaf.textchain.adventure;

import java.util.function.Function;

/**
 * Standard chain constructor: takes a
 * {@link WrappedTextComponentBuilder}
 * and creates a new chain instance.
 *
 * @param <C>   chain type
 */
@FunctionalInterface
public interface ChainConstructor<C extends Chain<C>> extends Function<WrappedTextComponentBuilder, C> {}
