package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/**
 * The de facto standard chain type, and also a factory.
 * All chains ultimately start here.
 */
public final class TextChain extends Chain<TextChain>
{
    /**
     * Constructs a new chain by supplying the constructor with its
     * required dependencies. The resulting chain will be the
     * same type as the constructor's generic type.
     *
     * @param constructor   a standard chain constructor
     * @return  a new chain created by the constructor
     */
    public static <C extends Chain<C>> C chain(ChainConstructor<C> constructor)
    {
        return constructor.apply(new WrappedTextComponentBuilder(Component.text()));
    }
    
    /**
     * Constructs a new chain from the source's constructor.
     * The resulting chain will be the same type as the
     * source's generic type.
     *
     * @param source    a chain source
     * @return  a new chain created by the source's constructor
     */
    public static <C extends Chain<C>> C chain(ChainSource<C> source)
    {
        return chain(source.getChainConstructor());
    }
    
    /**
     * Creates a new, empty {@link TextChain} instance.
     *
     * @return  an empty text chain
     */
    public static TextChain chain()
    {
        return chain(TextChain::new);
    }
    
    /**
     * Creates a new, empty {@link LegacyTextChain} instance.
     * This chain will automatically process legacy ampersand-style
     * colors codes in text (like {@code "&3&o"}).
     *
     * @return  an empty legacy text chain
     * @see TextProcessor#legacyAmpersand(String)
     */
    public static LegacyTextChain legacy()
    {
        return chain(LegacyTextChain::new);
    }
    
    /**
     * Constructs a new chain via the provided constructor,
     * but its formatting will be "reset" (it will be deliberately
     * {@link Chain#unformatted() unformatted} by explicitly disabling
     * various styles). Use this to avoid inheriting styles from a
     * parent chain/component (especially useful for item lore).
     *
     * <p><b>Note:</b> every element in this chain will inherit this
     * "reset" state unless specifically overridden by applying styles.</p>
     *
     * @param constructor   a standard chain constructor
     * @return  a new "reset" chain created by the constructor
     * @see #chain(ChainConstructor)
     */
    public static <C extends Chain<C>> C reset(ChainConstructor<C> constructor)
    {
        C chain = chain(constructor);
        return constructor.apply(chain.unformatted().getBuilder().peekOrCreateChild());
    }
    
    /**
     * Constructs a new chain via the source's constructor,
     * but its formatting will be "reset" (it will be deliberately
     * {@link Chain#unformatted() unformatted} by explicitly disabling
     * various styles). Use this to avoid inheriting styles from a
     * parent chain/component (especially useful for item lore).
     *
     * <p><b>Note:</b> every element in this chain will inherit this
     * "reset" state unless specifically overridden by applying styles.</p>
     *
     * @param source    a chain source
     * @return  a new "reset" chain created by the source's constructor
     * @see #chain(ChainSource)
     */
    public static <C extends Chain<C>> C reset(ChainSource<C> source)
    {
        return reset(source.getChainConstructor());
    }
    
    /**
     * Constructs a new, empty {@link TextChain} instance,
     * but its formatting will be "reset" (it will be deliberately
     * {@link Chain#unformatted() unformatted} by explicitly disabling
     * various styles). Use this to avoid inheriting styles from a
     * parent chain/component (especially useful for item lore).
     *
     * <p><b>Note:</b> every element in this chain will inherit this
     * "reset" state unless specifically overridden by applying styles.</p>
     *
     * @return  an empty "reset" text chain
     * @see #chain()
     */
    public static TextChain reset()
    {
        return reset(TextChain::new);
    }
    
    /**
     * Wraps an existing {@link WrappedTextComponentBuilder} instance
     * with a new {@link TextChain}.
     *
     * @param builder   an existing builder
     * @return  a new text chain containing the builder
     */
    public static TextChain wrap(WrappedTextComponentBuilder builder)
    {
        return new TextChain(builder);
    }
    
    /**
     * Wraps an existing chain with a new {@link TextChain}.
     * This is primarily useful for easily converting to the "standard"
     * chain type, as other chain subtypes may not necessarily be
     * adequately supported. This is mainly a defensive measure
     * to make your life easier, just in case.
     *
     * <p>Rather than using this method to gain a common chain type
     * everywhere, it's best to first try using <b>proper generics</b>.
     * If all else fails, then by all means, use this
     * (it's intended to be a last resort).</p>
     *
     * @param chain an existing chain
     * @return  a new text chain containing the existing
     *          chain's internal builder
     */
    public static <C extends Chain<C>> TextChain wrap(C chain)
    {
        return wrap(chain.getBuilder());
    }
    
    /**
     * Make a new TextChain, but {@link #chain()} is better.
     */
    public TextChain(WrappedTextComponentBuilder builder) { super(builder); }
    
    @Override
    protected ChainConstructor<TextChain> getConstructor() { return TextChain::new; }
    
    @Override
    protected TextComponent processText(String text) { return TextProcessor.none(text); }
}
