package community.leaf.textchain.adventure;

import java.util.function.Consumer;

public class ConditionalChains
{
    private ConditionalChains() { throw new UnsupportedOperationException(); }
    
    public static <C extends TextChain<C>> Consumer<? super C> ifNotEmpty(String text)
    {
        return chain -> { if (!text.isEmpty()) { chain.then(text); }};
    }
}
