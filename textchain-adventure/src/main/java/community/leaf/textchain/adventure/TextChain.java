package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;

/**
 * The de facto standard chain type, and also a factory.
 *
 * <p><b>All</b> chains ultimately start here.</p>
 *
 * <p>As a chain type, it doesn't do anything special
 * or unexpected. Text appended to this chain will remain
 * intact and unprocessed (by using
 * {@link TextProcessor#none(String)}).</p>
 *
 * <p>Think of it as the chain closest to Adventure's
 * own component builders.</p>
 */
public final class TextChain extends Chain<TextChain>
{
    /**
     * Constructs a new chain by wrapping the provided
     * text component builder and supplying it to the
     * constructor. Operations performed on the
     * newly-created chain will also modify the builder's
     * state, since the same builder instance
     * will be used internally.
     *
     * @param constructor   a standard chain constructor
     * @param builder       an existing text component builder
     * @param <C>   chain type
     * @return  a new chain created by the constructor
     *          that internally uses the provided builder
     */
    public static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, TextComponent.Builder builder)
    {
        return constructor.apply(new WrappedTextComponentBuilder(builder));
    }
    
    /**
     * Constructs a new chain by wrapping the provided
     * text component builder and supplying it to the
     * source's constructor. Operations performed on the
     * newly-created chain will also modify the builder's
     * state, since the same builder instance
     * will be used internally.
     *
     * @param source    a chain source
     * @param builder   an existing text component builder
     * @param <C>   chain type
     * @return  a new chain created by the source's constructor
     *          that internally uses the provided builder
     */
    public static <C extends Chain<C>> C chain(ChainSource<C> source, TextComponent.Builder builder)
    {
        return chain(source.getChainConstructor(), builder);
    }
    
    /**
     * Constructs a new {@link TextChain} instance
     * by wrapping the provided text component builder.
     * Operations performed on the newly-created chain will
     * also modify the builder's state, since the same
     * builder instance will be used internally.
     *
     * @param builder   an existing text component builder
     * @return  a new text chain
     *          that internally uses the provided builder
     */
    public static TextChain chain(TextComponent.Builder builder)
    {
        return chain(TextChain::new, builder);
    }
    
    /**
     * Constructs a new {@link LegacyTextChain} instance
     * by wrapping the provided text component builder.
     * Operations performed on the newly-created chain will
     * also modify the builder's state, since the same
     * builder instance will be used internally.
     *
     * @param builder   an existing text component builder
     * @return  a new legacy text chain
     *          that internally uses the provided builder
     *
     * @see #legacy()
     */
    public static LegacyTextChain legacy(TextComponent.Builder builder)
    {
        return chain(LegacyTextChain::new, builder);
    }
    
    /**
     * Constructs a new chain by supplying the constructor
     * with a new, empty text component builder.
     *
     * @param constructor   a standard chain constructor
     * @param <C>   chain type
     * @return  a new chain created by the constructor
     */
    public static <C extends Chain<C>> C chain(ChainConstructor<C> constructor)
    {
        return chain(constructor, Component.text());
    }
    
    /**
     * Constructs a new chain from the source's constructor by
     * supplying it with a new, empty text component builder.
     *
     * @param source    a chain source
     * @param <C>   chain type
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
     * color codes in text (like {@code "&3&o"}).
     *
     * @return  an empty legacy text chain
     * @see TextProcessor#legacyAmpersand(String)
     */
    public static LegacyTextChain legacy()
    {
        return chain(LegacyTextChain::new);
    }
    
    /**
     * Constructs a new chain by applying the provided
     * style to a new text component builder and
     * supplying it to the constructor. All elements
     * in the chain will inherit the style unless
     * specifically overridden by applying other styles.
     *
     * @param constructor   a standard chain constructor
     * @param style         the style to apply on the entire chain
     * @param <C>   chain type
     * @return  a new chain created by the constructor
     *          with the provided style applied
     */
    public static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, Style style)
    {
        return chain(constructor, Component.text().style(style));
    }
    
    /**
     * Constructs a new chain by applying the provided
     * style to a new text component builder and
     * supplying it to the source's constructor. All
     * elements in the chain will inherit the style unless
     * specifically overridden by applying other styles.
     *
     * @param source    a chain source
     * @param style     the style to apply on the entire chain
     * @param <C>   chain type
     * @return  a new chain created by the source's constructor
     *          with the provided style applied
     */
    public static <C extends Chain<C>> C chain(ChainSource<C> source, Style style)
    {
        return chain(source.getChainConstructor(), style);
    }
    
    /**
     * Creates a new {@link TextChain} instance
     * by applying the provided style to the chain's
     * internal text component builder. All elements in
     * the chain will inherit the style unless specifically
     * overridden by applying other styles.
     *
     * @param style     the style to apply on the entire chain
     * @return  a new text chain
     *          with the provided style applied
     */
    public static TextChain chain(Style style)
    {
        return chain(TextChain::new, style);
    }
    
    /**
     * Creates a new {@link LegacyTextChain} instance
     * by applying the provided style to the chain's
     * internal text component builder. All elements in
     * the chain will inherit the style unless specifically
     * overridden by applying other styles.
     *
     * @param style     the style to apply on the entire chain
     * @return  a new legacy text chain
     *          with the provided style applied
     *
     * @see #legacy()
     */
    public static LegacyTextChain legacy(Style style)
    {
        return chain(LegacyTextChain::new, style);
    }
    
    /**
     * Constructs a new chain by deriving a new text
     * component builder from the provided component-like
     * and supplying it to the constructor. If the component
     * received by {@link ComponentLike#asComponent()} is
     * a {@link TextComponent} instance, the chain's internal
     * builder will be that component converted directly into
     * a builder via {@link TextComponent#toBuilder()}.
     * Otherwise, a new text component builder is created
     * and the provided component-like is simply
     * appended to it.
     *
     * @param constructor       a standard chain constructor
     * @param componentLike     an existing component-like
     * @param <C>   chain type
     * @return  a new chain created by the constructor
     *          that internally uses a text component builder
     *          derived from the provided component-like
     */
    public static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        
        TextComponent.Builder builder = (component instanceof TextComponent)
            ? ((TextComponent) component).toBuilder()
            : Component.text().append(component);
        
        return chain(constructor, builder);
    }
    
    /**
     * Constructs a new chain by deriving a new text
     * component builder from the provided component-like
     * and supplying it to the source's constructor. If the
     * component received by {@link ComponentLike#asComponent()}
     * is a {@link TextComponent} instance, the chain's internal
     * builder will be that component converted directly into
     * a builder via {@link TextComponent#toBuilder()}.
     * Otherwise, a new text component builder is created
     * and the provided component-like is simply
     * appended to it.
     *
     * @param source            a chain source
     * @param componentLike     an existing component-like
     * @param <C>   chain type
     * @return  a new chain created by the source's constructor
     *          that internally uses a text component builder
     *          derived from the provided component-like
     */
    public static <C extends Chain<C>> C chain(ChainSource<C> source, ComponentLike componentLike)
    {
        return chain(source.getChainConstructor(), componentLike);
    }
    
    /**
     * Creates a new {@link TextChain} instance
     * by deriving a new text component builder from the
     * provided component-like. If the component received
     * by {@link ComponentLike#asComponent()} is a
     * {@link TextComponent} instance, the chain's internal
     * builder will be that component converted directly into
     * a builder via {@link TextComponent#toBuilder()}.
     * Otherwise, a new text component builder is created
     * and the provided component-like is simply
     * appended to it.
     *
     * @param componentLike     an existing component-like
     * @return  a new text chain
     *          that internally uses a text component builder
     *          derived from the provided component-like
     */
    public static TextChain chain(ComponentLike componentLike)
    {
        return chain(TextChain::new, componentLike);
    }
    
    /**
     * Creates a new {@link LegacyTextChain} instance
     * by deriving a new text component builder from the
     * provided component-like. If the component received
     * by {@link ComponentLike#asComponent()} is a
     * {@link TextComponent} instance, the chain's internal
     * builder will be that component converted directly into
     * a builder via {@link TextComponent#toBuilder()}.
     * Otherwise, a new text component builder is created
     * and the provided component-like is simply
     * appended to it.
     *
     * @param componentLike     an existing component-like
     * @return  a new text chain
     *          that internally uses a text component builder
     *          derived from the provided component-like
     *
     * @see #legacy()
     */
    public static LegacyTextChain legacy(ComponentLike componentLike)
    {
        return chain(LegacyTextChain::new, componentLike);
    }
    
    /**
     * Constructs a new chain by supplying the
     * constructor with a new, explicitly-unformatted
     * text component builder (thus "resetting" its style).
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @param constructor   a standard chain constructor
     * @param <C>   chain type
     * @return  a new "reset" chain created by the constructor
     *
     * @see #chain(ChainConstructor, Style)
     * @see Components#UNFORMATTED
     */
    public static <C extends Chain<C>> C reset(ChainConstructor<C> constructor)
    {
        return chain(constructor, Components.UNFORMATTED);
    }
    
    /**
     * Constructs a new chain by supplying the source's
     * constructor with a new, explicitly-unformatted
     * text component builder (thus "resetting" its style).
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @param source    a chain source
     * @param <C>   chain type
     * @return  a new "reset" chain created by the source's constructor
     *
     * @see #chain(ChainSource, Style)
     * @see Components#UNFORMATTED
     */
    public static <C extends Chain<C>> C reset(ChainSource<C> source)
    {
        return reset(source.getChainConstructor());
    }
    
    /**
     * Creates a new {@link TextChain} instance
     * with a new, explicitly-unformatted text component
     * builder (thus "resetting" its style).
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @return  a new "reset" text chain
     *
     * @see #chain(Style)
     * @see Components#UNFORMATTED
     */
    public static TextChain reset()
    {
        return reset(TextChain::new);
    }
    
    /**
     * Creates a new {@link LegacyTextChain} instance
     * with a new, explicitly-unformatted text component
     * builder (thus "resetting" its style).
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @return  a new "reset" legacy text chain
     *
     * @see #legacy(Style)
     * @see Components#UNFORMATTED
     */
    public static LegacyTextChain legacyReset()
    {
        return reset(LegacyTextChain::new);
    }
    
    /**
     * Wraps an existing {@link WrappedTextComponentBuilder}
     * instance with a new {@link TextChain}.
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
     * This is primarily useful for easily converting to the
     * "standard" chain type, as other chain subtypes may
     * not necessarily be adequately supported. This is
     * mainly a defensive measure to make your life easier,
     * just in case.
     *
     * <p>Rather than using this method to gain a common
     * chain type everywhere, it's best to first try using
     * <b>proper generics</b>. If all else fails, then by
     * all means, use this (it's intended to be
     * a last resort).</p>
     *
     * @param chain an existing chain
     * @param <C>   chain type
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
