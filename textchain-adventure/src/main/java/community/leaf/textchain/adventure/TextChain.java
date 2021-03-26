package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public final class TextChain extends Chain<TextChain>
{
    public static <C extends Chain<C>> C chain(ChainConstructor<C> constructor)
    {
        return constructor.apply(new WrappedTextComponentBuilder(Component.text()));
    }
    
    public static <C extends Chain<C>> C chain(ChainSource<C> source)
    {
        return TextChain.chain(source.getChainConstructor());
    }
    
    public static TextChain chain()
    {
        return TextChain.chain(TextChain::new);
    }
    
    public static <C extends Chain<C>> C reset(ChainConstructor<C> constructor)
    {
        C chain = TextChain.chain(constructor);
        return constructor.apply(chain.unformatted().getBuilder().peekOrCreateChild());
    }
    
    public static <C extends Chain<C>> C reset(ChainSource<C> source)
    {
        return TextChain.reset(source.getChainConstructor());
    }
    
    public static TextChain reset()
    {
        return TextChain.reset(TextChain::new);
    }
    
    public static TextChain wrap(WrappedTextComponentBuilder builder)
    {
        return new TextChain(builder);
    }
    
    public static <C extends Chain<C>> TextChain wrap(C chain)
    {
        return wrap(chain.getBuilder());
    }
    
    public TextChain(WrappedTextComponentBuilder builder) { super(builder); }
    
    @Override
    protected ChainConstructor<TextChain> getConstructor() { return TextChain::new; }
    
    @Override
    protected TextComponent processText(String text) { return TextProcessor.none(text); }
}
