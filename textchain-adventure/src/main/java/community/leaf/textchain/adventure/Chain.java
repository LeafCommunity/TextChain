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
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.RGBLike;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A chain of text components.
 *
 * @param <C>   self-returning chain subtype
 *              (for method chaining)
 */
@SuppressWarnings("unused")
public interface Chain<C extends Chain<C>> extends ComponentLike, ChainedAudienceSender<C>
{
    @SuppressWarnings("unchecked")
    private C self() { return (C) this; }
    
    /**
     * Gets the internal wrapped text component
     * builder that this chain manipulates.
     *
     * @return  this chain's internal wrapped builder
     */
    LinearTextComponentBuilder builder();
    
    // TODO: documentation
    TextProcessor processor();
    
    /**
     * Gets this chain's constructor so more
     * chains of the same type can be created
     * and operated on. If a subtype has more
     * required constructor arguments than
     * just the standard
     * {@link LinearTextComponentBuilder},
     * simply pass those dependencies along.
     *
     * @return  a valid standard constructor
     *          for the chain's type
     */
    ChainConstructor<C> constructor();
    
    /**
     * Builds the chain.
     *
     * @return  a text chain
     *
     * @see LinearTextComponentBuilder#asComponent()
     */
    @Override
    default TextComponent asComponent() { return builder().asComponent(); }
    
    
    /**
     * Builds the chain as a list of components.
     * Since chains inherently operate on a
     * component builder's {@code extra} (its
     * {@link Component#children() children}),
     * most chain elements should remain intact.
     * However, non-style aspects of this chain's
     * root component builder will be lost.
     *
     * <p><b>Note:</b> this method is inadequate
     * for item lore because it not only preserves
     * new line characters ({@code "\n"}), but
     * <i>every</i> child component is a distinct
     * list entry.</p>
     *
     * <p>For a more lore-friendly list, use
     * {@link #asComponentListSplitByNewLine()}
     * instead.</p>
     *
     * @return  the chain as a flattened list of
     *          components
     *
     * @see Components#flattenExtra(Component)
     */
    default List<Component> asComponentList()
    {
        return Components.flattenExtra(asComponent());
    }
    
    /**
     * Builds the chain as a list of components
     * by combining components until a new line
     * component (containing only {@code "\n"})
     * is encountered. Since chains inherently
     * operate on a component builder's {@code extra}
     * (its {@link Component#children() children}),
     * most chain elements should remain intact.
     * However, non-style aspects of this chain's
     * root component builder will be lost.
     *
     * <p><b>Note:</b> to obtain new line
     * components, use the {@code next} methods
     * like: {@link #nextLine()}.</p>
     *
     * @return  the chain as a flattened list of
     *          components combined until a
     *          new line is encountered
     *
     * @see Components#flattenExtraSplitByNewLine(Component)
     */
    default List<Component> asComponentListSplitByNewLine()
    {
        return Components.flattenExtraSplitByNewLine(asComponent());
    }
    
    /**
     * Provides the consumer with {@code this} chain.
     * Useful for applying modifications of some sort
     * to the chain.
     *
     * @param consumer  a consumer
     * @return  self (for method chaining)
     */
    default C apply(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(self());
        return self();
    }
    
    /**
     * Maps {@code this} chain to something else.
     *
     * @param mapper    mapper function
     * @param <V>   value type
     * @return  resulting value of the mapper function
     */
    default  <@NullOr V> V map(Function<? super C, V> mapper)
    {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply(self());
    }
    
    /**
     * Provides the consumer with a chain that
     * operates on the <b>latest</b> chain
     * element's {@code extra} (its {@link
     * Component#children() children}). Use this
     * to create a <b>group</b> of chain elements
     * that will inherit the formatting and style
     * from the latest existing chain element
     * (or create one if none exist).
     *
     * @param consumer  extra consumer
     * @return  self (for method chaining)
     */
    default C extra(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        
        consumer.accept(builder().peekOrCreateChild()
            .into(builder -> constructor().construct(builder, processor())));
        
        return self();
    }
    
    /**
     * Provides the consumer with a chain that
     * operates on a <b>new</b> chain element's
     * {@code extra} (its {@link Component#children()
     * children}). Use this to create a <b>group</b>
     * of chain elements that will inherit the
     * formatting and style from this new
     * chain element.
     *
     * @param consumer  extra consumer
     * @return  self (for method chaining)
     */
    default C thenExtra(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        
        consumer.accept(builder().createNextChild()
            .into(builder -> constructor().construct(builder, processor())));
        
        return self();
    }
    
    /**
     * Creates a <b>new</b> chain element
     * containing the provided component-like.
     * If the component obtained from the argument
     * is a {@link TextComponent} instance, it
     * will be converted to a builder and added
     * as the next chain element. Otherwise, a new,
     * empty text component builder will be created
     * and the component will simply be appended to
     * it (as an {@code extra} child).
     *
     * @param componentLike the component-like
     *                      to append
     * @return  self (for method chaining)
     */
    default C then(ComponentLike componentLike)
    {
        Component component = Components.safelyAsComponent(componentLike);
        
        if (component instanceof TextComponent)
        {
            builder().createNextChildWithBuilder(((TextComponent) component).toBuilder());
        }
        else
        {
            builder().createNextChild().getComponentBuilder().append(component);
        }
        
        return self();
    }
    
    /**
     * Creates a <b>new</b> empty chain element.
     *
     * @return  self (for method chaining)
     */
    default C thenEmpty() { return then(Component.empty()); }
    
    /**
     * Creates a <b>new</b> chain element by
     * processing the input text with the
     * provided processor.
     *
     * @param text          the text to append
     * @param processor     text processor
     * @return  self (for method chaining)
     */
    default C then(String text, TextProcessor processor)
    {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(processor, "processor");
        return then(processor.process(text));
    }
    
    /**
     * Creates a <b>new</b> chain element by first
     * {@link #processor()}  processing}
     * the input text using the chain's default
     * processor. It's important to remember that
     * different chain types will process the text
     * differently. To skip processing altogether,
     * use {@link #thenUnprocessed(String)}.
     *
     * @param text  the text to append
     * @return  self (for method chaining)
     */
    default C then(String text) { return then(text, processor()); }
    
    /**
     * Creates a <b>new</b> chain element by
     * directly converting it to a new text
     * component, skipping processing entirely.
     *
     * @param text  the text to append
     * @return  self (for method chaining)
     */
    default C thenUnprocessed(String text) { return then(text, TextProcessor.none()); }
    
    /**
     * Inserts a new line by creating a <b>new</b>
     * chain element that <i>only</i> contains the
     * new line character ({@code "\n"}).
     *
     * @return  self (for method chaining)
     *
     * @see Component#newline()
     */
    default C nextLine() { return then(Component.newline()); }
    
    /**
     * Creates <b>new</b> chain elements by
     * inserting a {@link #nextLine() new line}
     * then appending the provided
     * {@link #then(ComponentLike) component-like}.
     *
     * <p><b>Note:</b> The inserted new line
     * is its own distinct chain element. This
     * method is equivalent to calling:
     * {@code .nextLine().then(componentLike)}</p>
     *
     * @param componentLike the component-like
     *                      to append
     * @return  self (for method chaining)
     *
     * @see #nextLine()
     * @see #then(ComponentLike)
     */
    default C next(ComponentLike componentLike) { return nextLine().then(componentLike); }
    
    /**
     * Creates <b>new</b> chain elements by
     * inserting a {@link #nextLine() new line}
     * then appending {@link #then(String, TextProcessor)
     * the text} using the provided processor.
     *
     * <p><b>Note:</b> The inserted new line
     * is its own distinct chain element. This
     * method is equivalent to calling:
     * {@code .nextLine().then(text, processor)}</p>
     *
     * @param text          the text to append
     * @param processor     text processor
     * @return  self (for method chaining)
     *
     * @see #nextLine()
     * @see #then(String, TextProcessor)
     */
    default C next(String text, TextProcessor processor) { return nextLine().then(text, processor); }
    
    /**
     * Creates <b>new</b> chain elements by
     * inserting a {@link #nextLine() new line}
     * then appending the {@link #then(String)
     * input text} using the chain's default
     * processor. It's important to remember that
     * different chain types will process the text
     * differently. To skip processing altogether,
     * use {@link #nextUnprocessed(String)}.
     *
     * <p><b>Note:</b> The inserted new line
     * is its own distinct chain element. This
     * method is equivalent to calling:
     * {@code .nextLine().then(text)}</p>
     *
     * @param text  the text to append
     * @return  self (for method chaining)
     *
     * @see #nextLine()
     * @see #then(String)
     */
    default C next(String text) { return nextLine().then(text); }
    
    /**
     * Creates <b>new</b> chain elements by
     * inserting a {@link #nextLine() new line}
     * then directly appending the
     * {@link #thenUnprocessed(String) input
     * text}, skipping processing entirely.
     *
     * <p><b>Note:</b> The inserted new line
     * is its own distinct chain element. This
     * method is equivalent to calling:
     * {@code .nextLine().thenUnprocessed(text)}</p>
     *
     * @param text  the text to append
     * @return  self (for method chaining)
     *
     * @see #nextLine()
     * @see #thenUnprocessed(String)
     */
    default C nextUnprocessed(String text) { return nextLine().thenUnprocessed(text); }
    
    /**
     * Sets the text content of the
     * <b>latest</b> chain element,
     * overwriting whatever it was
     * previously set to.
     *
     * @param text  the text to set
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#content(String)
     */
    default C overwriteText(String text)
    {
        Objects.requireNonNull(text, "text");
        builder().peekThenApply(child -> child.content(text));
        return self();
    }
    
    /**
     * Sets the style of the
     * <b>latest</b> chain element,
     * overwriting any previously-set
     * styling options (including click
     * and hover events).
     *
     * @param style     the style
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#style(Style)
     */
    default C overwriteStyle(Style style)
    {
        Objects.requireNonNull(style, "style");
        builder().peekThenApply(child -> child.style(style));
        return self();
    }
    
    /**
     * Merges the provided style into the
     * <b>latest</b> chain element's
     * existing styling options.
     *
     * @param style     the style
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#style(Consumer)
     * @see Style.Builder#merge(Style)
     */
    default C style(Style style)
    {
        Objects.requireNonNull(style, "style");
        builder().peekThenApply(child -> child.style(styleBuilder -> styleBuilder.merge(style)));
        return self();
    }
    
    /**
     * Disables most formatting options on the
     * <b>latest</b> chain element by setting
     * all {@link TextDecoration decoration types}
     * to {@code false}.
     *
     * @return  self (for method chaining)
     *
     * @see Components#UNFORMATTED
     * @see #format(TextDecoration, boolean)
     */
    default C unformatted() { return style(Components.UNFORMATTED); }
    
    /**
     * Sets the color of the
     * <b>latest</b> chain element.
     *
     * @param color     the color
     * @return  self (for method chaining)
     */
    default C color(RGBLike color)
    {
        Objects.requireNonNull(color, "color");
        TextColor result;
        
        if (color instanceof TextColor) { result = (TextColor) color; }
        else if (color instanceof ColorSource) { result = ((ColorSource) color).color(); }
        else { result = TextColor.color(color); }
        
        builder().peekThenApply(child -> child.color(result));
        return self();
    }
    
    /**
     * Formats the <b>latest</b> chain element
     * with the provided decoration. The
     * decoration state will be set to:
     * {@link TextDecoration.State#TRUE}.
     *
     * @param decoration    the format to apply
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#decorate(TextDecoration)
     */
    default C format(TextDecoration decoration)
    {
        Objects.requireNonNull(decoration, "decoration");
        builder().peekThenApply(child -> child.decorate(decoration));
        return self();
    }
    
    // TODO: documentation
    default C format(LegacyColorAlias code)
    {
        Objects.requireNonNull(code, "code");
        if (code.isColor()) { code.asColor().ifPresent(this::color); }
        else if (code.isDecoration()) { code.asDecoration().ifPresent(this::format); }
        return self();
    }
    
    /**
     * Formats the <b>latest</b> chain element
     * with all provided decorations. Each
     * decoration state will be set to:
     * {@link TextDecoration.State#TRUE}.
     *
     * @param decorations   the formats to apply
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#decorate(TextDecoration...)
     */
    default C format(TextDecoration ... decorations)
    {
        Objects.requireNonNull(decorations, "decorations");
        builder().peekThenApply(child -> child.decorate(decorations));
        return self();
    }
    
    /**
     * Formats the <b>latest</b> chain element
     * with the provided decoration by explicitly
     * enabling or disabling it. If the state is
     * {@code true}, the decoration will be
     * enabled. Otherwise, {@code false} will
     * disable the decoration even if a parent
     * chain/component has the decoration enabled.
     *
     * @param decoration    the format type
     * @param state         the state to set
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#decoration(TextDecoration, boolean)
     */
    default C format(TextDecoration decoration, boolean state)
    {
        Objects.requireNonNull(decoration, "decoration");
        builder().peekThenApply(child -> child.decoration(decoration, state));
        return self();
    }
    
    /**
     * Formats the <b>latest</b> chain element
     * with the provided decoration by setting
     * it to the specified state.
     *
     * @param decoration    the format type
     * @param state         the state to set
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#decoration(TextDecoration, TextDecoration.State)
     */
    default C format(TextDecoration decoration, TextDecoration.State state)
    {
        Objects.requireNonNull(decoration, "decoration");
        Objects.requireNonNull(state, "state");
        builder().peekThenApply(child -> child.decoration(decoration, state));
        return self();
    }
    
    /**
     * Formats the <b>latest</b> chain element
     * by making it bold.
     *
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration)
     * @see TextDecoration#BOLD
     */
    default C bold() { return format(TextDecoration.BOLD); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by explicitly enabling or disabling its
     * bold decoration.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, boolean)
     * @see TextDecoration#BOLD
     */
    default C bold(boolean state) { return format(TextDecoration.BOLD, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by setting its bold decoration to the
     * specified state.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, TextDecoration.State)
     * @see TextDecoration#BOLD
     */
    default C bold(TextDecoration.State state) { return format(TextDecoration.BOLD, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by making it italic.
     *
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration)
     * @see TextDecoration#ITALIC
     */
    default C italic() { return format(TextDecoration.ITALIC); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by explicitly enabling or disabling its
     * italic decoration.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, boolean)
     * @see TextDecoration#ITALIC
     */
    default C italic(boolean state) { return format(TextDecoration.ITALIC, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by setting its italic decoration to the
     * specified state.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, TextDecoration.State)
     * @see TextDecoration#ITALIC
     */
    default C italic(TextDecoration.State state) { return format(TextDecoration.ITALIC, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by making it obfuscated.
     *
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration)
     * @see TextDecoration#OBFUSCATED
     */
    default C obfuscated() { return format(TextDecoration.OBFUSCATED); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by explicitly enabling or disabling its
     * obfuscated decoration.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, boolean)
     * @see TextDecoration#OBFUSCATED
     */
    default C obfuscated(boolean state) { return format(TextDecoration.OBFUSCATED, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by setting its obfuscated decoration to the
     * specified state.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, TextDecoration.State)
     * @see TextDecoration#OBFUSCATED
     */
    default C obfuscated(TextDecoration.State state) { return format(TextDecoration.OBFUSCATED, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by making it strikethrough.
     *
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration)
     * @see TextDecoration#STRIKETHROUGH
     */
    default C strikethrough() { return format(TextDecoration.STRIKETHROUGH); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by explicitly enabling or disabling its
     * strikethrough decoration.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, boolean)
     * @see TextDecoration#STRIKETHROUGH
     */
    default C strikethrough(boolean state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by setting its strikethrough decoration
     * to the specified state.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, TextDecoration.State)
     * @see TextDecoration#STRIKETHROUGH
     */
    default C strikethrough(TextDecoration.State state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by making it underlined.
     *
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration)
     * @see TextDecoration#UNDERLINED
     */
    default C underlined() { return format(TextDecoration.UNDERLINED); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by explicitly enabling or disabling its
     * underlined decoration.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, boolean)
     * @see TextDecoration#UNDERLINED
     */
    default C underlined(boolean state) { return format(TextDecoration.UNDERLINED, state); }
    
    /**
     * Formats the <b>latest</b> chain element
     * by setting its underlined decoration to
     * the specified state.
     *
     * @param state     the state to set
     * @return  self (for method chaining)
     *
     * @see #format(TextDecoration, TextDecoration.State)
     * @see TextDecoration#UNDERLINED
     */
    default C underlined(TextDecoration.State state) { return format(TextDecoration.UNDERLINED, state); }
    
    /**
     * Sets the click event of the
     * <b>latest</b> chain element.
     * Any previous click event on the
     * element will be overwritten.
     *
     * @param event     a click event
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#clickEvent(ClickEvent)
     */
    default C click(ClickEvent event)
    {
        Objects.requireNonNull(event, "event");
        builder().peekThenApply(child -> child.clickEvent(event));
        return self();
    }
    
    /**
     * Sets the click event of the
     * <b>latest</b> chain element to
     * run the provided command.
     *
     * @param command   the command to run
     *                  when clicked
     * @return  self (for method chaining)
     *
     * @see #click(ClickEvent)
     */
    default C command(String command)
    {
        Objects.requireNonNull(command, "command");
        return click(ClickEvent.runCommand(command));
    }
    
    /**
     * Sets the click event of the
     * <b>latest</b> chain element to
     * suggest the provided text.
     *
     * @param suggestion    the suggestion to
     *                      display when clicked
     * @return  self (for method chaining)
     *
     * @see #click(ClickEvent)
     */
    default C suggest(String suggestion)
    {
        Objects.requireNonNull(suggestion, "suggestion");
        return click(ClickEvent.suggestCommand(suggestion));
    }
    
    /**
     * Sets the click event of the
     * <b>latest</b> chain element to
     * open the provided link (URL).
     *
     * @param link  the link to open
     *              when clicked
     * @return  self (for method chaining)
     *
     * @see #click(ClickEvent)
     */
    default C link(String link)
    {
        Objects.requireNonNull(link, "link");
        return click(ClickEvent.openUrl(link));
    }
    
    /**
     * Sets the insertion of the
     * <b>latest</b> chain element.
     *
     * @param insertion     the text to insert
     *                      when shift + clicked
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#insertion(String)
     */
    default C insertion(String insertion)
    {
        Objects.requireNonNull(insertion, "insertion");
        builder().peekThenApply(child -> child.insertion(insertion));
        return self();
    }
    
    /**
     * Sets the hover event of the
     * <b>latest</b> chain element.
     * Any previous hover event on the
     * element will be overwritten.
     *
     * @param eventSource   a hover event source
     * @return  self (for method chaining)
     *
     * @see TextComponent.Builder#hoverEvent(HoverEventSource)
     */
    default C hover(HoverEventSource<?> eventSource)
    {
        Objects.requireNonNull(eventSource, "eventSource");
        builder().peekThenApply(child -> child.hoverEvent(eventSource));
        return self();
    }
    
    /**
     * Sets the hover event of the
     * <b>latest</b> chain element
     * to a text tooltip containing
     * the provided component-like.
     *
     * @param componentLike     the tooltip component
     * @return  self (for method chaining)
     *
     * @see #hover(HoverEventSource)
     */
    default C tooltip(ComponentLike componentLike)
    {
        return hover(HoverEvent.showText(Components.safelyAsComponent(componentLike)));
    }
    
    /**
     * Sets the hover event of the
     * <b>latest</b> chain element
     * to a text tooltip as a result of
     * supplying the provided consumer
     * with a new, empty chain for it
     * to manipulate. Once the consumer is
     * finished modifying the fresh chain,
     * it is then converted into a
     * component and set as the tooltip.
     * The received chain is the same generic
     * type as the original chain since it
     * uses the same {@link #constructor()
     * constructor}.
     *
     * @param tooltipConsumer   consumer that modifies
     *                          an empty chain that will
     *                          then become the tooltip
     * @return  self (for method chaining)
     *
     * @see #hover(HoverEventSource)
     */
    default C tooltip(Consumer<? super C> tooltipConsumer)
    {
        Objects.requireNonNull(tooltipConsumer, "tooltipConsumer");
        C tooltipChain = constructor().construct(LinearTextComponentBuilder.empty(), processor());
        tooltipConsumer.accept(tooltipChain);
        return tooltip(tooltipChain);
    }
    
    /**
     * Sets the hover event of the
     * <b>latest</b> chain element
     * to a tooltip containing the
     * input text processed by the
     * provided processor.
     *
     * @param tooltipText   text to display
     *                      when hovered over
     * @param processor     text processor
     * @return  self (for method chaining)
     *
     * @see #hover(HoverEventSource)
     * @see #then(String, TextProcessor)
     */
    default C tooltip(String tooltipText, TextProcessor processor)
    {
        Objects.requireNonNull(tooltipText, "tooltipText");
        Objects.requireNonNull(processor, "processor");
        return tooltip(processor.process(tooltipText));
    }
    
    /**
     * Sets the hover event of the
     * <b>latest</b> chain element
     * to a tooltip containing the input
     * text processed by the chain's default
     * processor. It's important to remember that
     * different chain types will process the text
     * differently. To skip processing altogether,
     * use {@link #tooltipUnprocessed(String)}.
     *
     * @param tooltipText   text to display
     *                      when hovered over
     * @return  self (for method chaining)
     *
     * @see #hover(HoverEventSource)
     * @see #then(String)
     */
    default C tooltip(String tooltipText) { return tooltip(tooltipText, processor()); }
    
    /**
     * Sets the hover event of the
     * <b>latest</b> chain element
     * to a tooltip containing the input
     * text, skipping processing entirely.
     *
     * @param tooltipText   text to display
     *                      when hovered over
     * @return  self (for method chaining)
     *
     * @see #hover(HoverEventSource)
     * @see #thenUnprocessed(String)
     */
    default C tooltipUnprocessed(String tooltipText) { return tooltip(tooltipText, TextProcessor.none()); }
}
