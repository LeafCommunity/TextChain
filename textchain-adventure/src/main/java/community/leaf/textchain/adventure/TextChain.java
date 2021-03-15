package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

public final class TextChain extends Chain<TextChain>
{
    public static <C extends Chain<C>> C using(ChainConstructor<C> constructor)
    {
        return constructor.apply(new WrappedTextComponentBuilder(Component.text()));
    }
    
    public static <C extends Chain<C>> C using(ChainSource<C> source)
    {
        return TextChain.using(source.getChainConstructor());
    }
    
    public static TextChain empty() { return TextChain.using(TextChain::new); }
    
    public static TextChain of(String text) { return TextChain.empty().then(text); }
    
    public static TextChain of(ComponentLike componentLike) { return TextChain.empty().then(componentLike); }
    
    //
    //
    //
    
    public TextChain(WrappedTextComponentBuilder builder) { super(builder); }
    
    @Override
    protected ChainConstructor<TextChain> getConstructor() { return TextChain::new; }
}
