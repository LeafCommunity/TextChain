package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

import java.util.Objects;

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
    
    public static TextChain empty()
    {
        return TextChain.using(TextChain::new);
    }
    
    public static TextChain of(String text)
    {
        return TextChain.empty().then(text);
    }
    
    public static TextChain of(ComponentLike componentLike)
    {
        return TextChain.empty().then(componentLike);
    }
    
    public static TextChain wrap(WrappedTextComponentBuilder builder)
    {
        return new TextChain(builder);
    }
    
    public static <C extends Chain<C>> TextChain wrap(C chain)
    {
        return wrap(chain.getBuilder());
    }
    
    private TextChain(WrappedTextComponentBuilder builder) { super(Objects.requireNonNull(builder, "builder")); }
    
    @Override
    protected ChainConstructor<TextChain> getConstructor() { return TextChain::new; }
}
