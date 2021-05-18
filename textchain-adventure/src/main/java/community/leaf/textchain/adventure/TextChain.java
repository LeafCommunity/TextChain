/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import pl.tlinkowski.annotation.basic.NullOr;

/**
 * The de facto standard chain, and also a factory:
 * <b>all</b> chains ultimately start here.
 *
 * <p>As a chain type, it doesn't do anything special
 * or unexpected. Text appended to this chain will remain
 * intact and unprocessed (by using
 * {@link TextProcessor#none()}).</p>
 *
 * <p>Think of it as the chain type closest to
 * Adventure's own component builders. It's the
 * vanilla variety, if you will.</p>
 */
@SuppressWarnings("unused")
public interface TextChain extends Chain<TextChain>
{
    static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, TextComponent.Builder builder, TextProcessor processor)
    {
        return constructor.construct(LinearTextComponentBuilder.wrap(builder), processor);
    }
    
    static <C extends Chain<C>> C chain(ChainSource<C> source, TextComponent.Builder builder, TextProcessor processor)
    {
        return chain(source.getChainConstructor(), builder, processor);
    }
    
    /**
     * Constructs a new chain by wrapping the provided
     * text component builder and supplying it to the
     * constructor. Operations performed on the builder
     * will be reflected in the chain, since the same
     * builder instance will be used internally.
     *
     * @param constructor   a standard chain constructor
     * @param builder       an existing text component builder
     * @param <C>   chain type
     * @return  a new chain created by the constructor
     *          that internally uses the provided builder
     */
    static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, TextComponent.Builder builder)
    {
        return chain(constructor, builder, TextProcessor.none());
    }
    
    /**
     * Constructs a new chain by wrapping the provided
     * text component builder and supplying it to the
     * source's constructor. Operations performed on the
     * builder will be reflected in the chain, since the
     * same builder instance will be used internally.
     *
     * @param source    a chain source
     * @param builder   an existing text component builder
     * @param <C>   chain type
     * @return  a new chain created by the source's constructor
     *          that internally uses the provided builder
     */
    static <C extends Chain<C>> C chain(ChainSource<C> source, TextComponent.Builder builder)
    {
        return chain(source.getChainConstructor(), builder, TextProcessor.none());
    }
    
    /**
     * Constructs a new {@link TextChain} instance
     * by wrapping the provided text component builder.
     * Operations performed on the builder will be
     * reflected in the chain, since the same builder
     * instance will be used internally.
     *
     * @param builder   an existing text component builder
     * @return  a new text chain
     *          that internally uses the provided builder
     */
    static TextChain chain(TextComponent.Builder builder)
    {
        return chain(TextChainImpl::new, builder, TextProcessor.none());
    }
    
    /**
     * Constructs a new {@link TextChain} instance
     * by wrapping the provided text component builder.
     * Operations performed on the builder will be
     * reflected in the chain, since the same builder
     * instance will be used internally.
     *
     * @param builder   an existing text component builder
     * @return  a new legacy text chain
     *          that internally uses the provided builder
     *
     * @see #legacy()
     */
    static TextChain legacy(TextComponent.Builder builder)
    {
        return chain(TextChainImpl::new, builder, TextProcessor.legacyAmpersand());
    }
    
    static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, TextProcessor processor)
    {
        return chain(constructor, Component.text(), processor);
    }
    
    static <C extends Chain<C>> C chain(ChainSource<C> source, TextProcessor processor)
    {
        return chain(source.getChainConstructor(), processor);
    }
    
    /**
     * Constructs a new chain by supplying the constructor
     * with a new, empty text component builder.
     *
     * @param constructor   a standard chain constructor
     * @param <C>   chain type
     * @return  a new chain created by the constructor
     */
    static <C extends Chain<C>> C chain(ChainConstructor<C> constructor)
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
    static <C extends Chain<C>> C chain(ChainSource<C> source)
    {
        return chain(source.getChainConstructor());
    }
    
    /**
     * Creates a new, empty {@link TextChain} instance.
     *
     * @return  an empty text chain
     */
    static TextChain chain()
    {
        return chain(TextChainImpl::new);
    }
    
    /**
     * Creates a new, empty {@link TextChain} instance.
     * This chain will automatically process legacy ampersand-style
     * color codes in text (like {@code "&3&o"}).
     *
     * @return  an empty legacy text chain
     * @see TextProcessor#legacyAmpersand()
     */
    static TextChain legacy()
    {
        return chain(TextChainImpl::new, TextProcessor.legacyAmpersand());
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
    static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, Style style)
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
    static <C extends Chain<C>> C chain(ChainSource<C> source, Style style)
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
    static TextChain chain(Style style)
    {
        return chain(TextChainImpl::new, style);
    }
    
    /**
     * Creates a new {@link TextChain} instance
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
    static TextChain legacy(Style style)
    {
        // TODO: add processor
        return chain(TextChainImpl::new, style);
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
    static <C extends Chain<C>> C chain(ChainConstructor<C> constructor, ComponentLike componentLike)
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
    static <C extends Chain<C>> C chain(ChainSource<C> source, ComponentLike componentLike)
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
    static TextChain chain(ComponentLike componentLike)
    {
        return chain(TextChainImpl::new, componentLike);
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
     *
     * @see #legacy()
     */
    static TextChain legacy(ComponentLike componentLike)
    {
        return chain(TextChainImpl::new, componentLike);
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
     * @see Components#RESET
     */
    static <C extends Chain<C>> C reset(ChainConstructor<C> constructor)
    {
        return chain(constructor, Components.RESET);
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
     * @see Components#RESET
     */
    static <C extends Chain<C>> C reset(ChainSource<C> source)
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
     * @see Components#RESET
     */
    static TextChain reset()
    {
        return reset(TextChainImpl::new);
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
     * @return  a new "reset" legacy text chain
     *
     * @see #legacy(Style)
     * @see Components#RESET
     */
    static TextChain legacyReset()
    {
        // TODO: text processor
        return reset(TextChainImpl::new);
    }
    
    /**
     * Constructs a new chain by supplying the
     * constructor with a new, explicitly-unformatted
     * text component builder (thus "resetting" its style)
     * with the specified color applied.
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     * However, if the provided color is {@code null},
     * then the color from a parent chain/component will
     * be inherited.
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @param constructor   a standard chain constructor
     * @param color         the color to apply or null
     *                      to allow color inheritance
     * @param <C>   chain type
     * @return  a new "reset" chain created by the
     *          constructor with the color applied
     *
     * @see #chain(ChainConstructor, Style)
     * @see Components#UNFORMATTED
     */
    static <C extends Chain<C>> C reset(ChainConstructor<C> constructor, @NullOr TextColor color)
    {
        return chain(
            constructor,
            (color == null)
                ? Components.UNFORMATTED
                : Components.UNFORMATTED.color(color)
        );
    }
    
    /**
     * Constructs a new chain by supplying the source's
     * constructor with a new, explicitly-unformatted
     * text component builder (thus "resetting" its style)
     * with the specified color applied.
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     * However, if the provided color is {@code null},
     * then the color from a parent chain/component will
     * be inherited.
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @param source    a chain source
     * @param color     the color to apply or null
     *                  to allow color inheritance
     * @param <C>   chain type
     * @return  a new "reset" chain created by the source's
     *          constructor with the color applied
     *
     * @see #chain(ChainSource, Style)
     * @see Components#UNFORMATTED
     */
    static <C extends Chain<C>> C reset(ChainSource<C> source, @NullOr TextColor color)
    {
        return reset(source.getChainConstructor(), color);
    }
    
    /**
     * Creates a new {@link TextChain} instance
     * with a new, explicitly-unformatted text component
     * builder (thus "resetting" its style)
     * with the specified color applied.
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     * However, if the provided color is {@code null},
     * then the color from a parent chain/component will
     * be inherited.
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @param color     the color to apply or null
     *                  to allow color inheritance
     * @return  a new "reset" text chain
     *          with the color applied
     *
     * @see #chain(Style)
     * @see Components#UNFORMATTED
     */
    static TextChain reset(@NullOr TextColor color)
    {
        return reset(TextChainImpl::new, color);
    }
    
    /**
     * Creates a new {@link TextChain} instance
     * with a new, explicitly-unformatted text component
     * builder (thus "resetting" its style)
     * with the specified color applied.
     * Use this to avoid inheriting styles from a parent
     * chain/component (especially useful for item lore).
     * However, if the provided color is {@code null},
     * then the color from a parent chain/component will
     * be inherited.
     *
     * <p><b>Note:</b> every element in the chain inherits
     * this "reset" state unless specifically overridden
     * by applying styles.</p>
     *
     * @param color     the color to apply or null
     *                  to allow color inheritance
     * @return  a new "reset" legacy text chain
     *          with the color applied
     *
     * @see #legacy(Style)
     * @see Components#UNFORMATTED
     */
    static TextChain legacyReset(@NullOr TextColor color)
    {
        // todo: text processor
        return reset(TextChainImpl::new, color);
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
    static <C extends Chain<C>> TextChain wrap(C chain)
    {
        return (chain instanceof TextChain)
            ? (TextChain) chain
            : new TextChainImpl(chain.builder(), chain.processor());
    }
}
