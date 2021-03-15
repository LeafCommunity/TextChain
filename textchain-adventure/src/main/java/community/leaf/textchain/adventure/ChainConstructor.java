package community.leaf.textchain.adventure;

import java.util.function.Function;

public interface ChainConstructor<C extends Chain<C>> extends Function<WrappedTextComponentBuilder, C> {}
