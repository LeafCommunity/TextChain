package community.leaf.textchain.adventure;

import java.util.function.Consumer;

public class ConditionalChains
{
    private ConditionalChains() { throw new UnsupportedOperationException(); }
    
    static <T> T applyThenSupply(T thing, Consumer<T> consumer)
    {
        consumer.accept(thing);
        return thing;
    }
    
    public static Consumer<TextChain> ifNotEmpty(String text)
    {
        return chain -> { if (!text.isEmpty()) { chain.then(text); }};
    }
}
