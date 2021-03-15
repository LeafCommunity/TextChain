package community.leaf.textchain.adventure;

import java.util.function.Consumer;

public class ConditionalChains
{
    private ConditionalChains() { throw new UnsupportedOperationException(); }
    
    public static Consumer<TextComponentChain> ifNotEmpty(String text)
    {
        return chain -> { if (!text.isEmpty()) { chain.then(text); }};
    }
}
