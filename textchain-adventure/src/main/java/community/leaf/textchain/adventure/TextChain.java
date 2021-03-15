package community.leaf.textchain.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.RGBLike;

import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface TextChain<C extends TextChain<C>> extends ComponentLike
{
    static <C extends TextChain<C>> C using(TextChainConstructor<C> constructor)
    {
        return constructor.apply(new WrappedTextComponentBuilder(Component.text()));
    }
    
    static <C extends TextChain<C>> C using(TextChainSource<C> source)
    {
        return using(source.getTextChainConstructor());
    }
    
    static TextComponentChain empty() { return TextChain.using(TextComponentChain::new); }
    
    static TextComponentChain of(String text) { return TextChain.empty().then(text); }
    
    static TextComponentChain of(ComponentLike componentLike) { return TextChain.empty().then(componentLike); }
    
    WrappedTextComponentBuilder getBuilder();
    
    TextChainConstructor<C> getConstructor();
    
    @Override
    default TextComponent asComponent() { return getBuilder().asComponent(); }
    
    @SuppressWarnings("unchecked")
    default C self() { return (C) this; }
    
    default C apply(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(self());
        return self();
    }
    
    default C extra(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(getBuilder().peekOrCreateChild().wrap(getConstructor()));
        return self();
    }
    
    default C thenExtra(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(getBuilder().createNextChild().wrap(getConstructor()));
        return self();
    }
    
    default C then(TextComponent.Builder builder)
    {
        Objects.requireNonNull(builder, "builder");
        getBuilder().createNextChildWithBuilder(builder);
        return self();
    }
    
    default C then(ComponentLike componentLike)
    {
        Objects.requireNonNull(componentLike, "componentLike");
        Component component = Objects.requireNonNull(componentLike.asComponent(), "componentLike returned null");
        if (component instanceof TextComponent) { return then(((TextComponent) component).toBuilder()); }
        getBuilder().createNextChild().getComponentBuilder().append(component);
        return self();
    }
    
    default C then(String text, ColorParsers colors)
    {
        Objects.requireNonNull(text, "text");
        return then(colors.parse(text).toBuilder());
    }
    
    default C then(String text) { return then(text, ColorParsers.AMPERSAND); }
    
    default C thenUncolored(String text) { return then(text, ColorParsers.NONE); }
    
    default C nextLine() { return thenUncolored("\n"); }
    
    default C next(TextComponent.Builder builder) { return nextLine().then(builder); }
    
    default C next(ComponentLike componentLike) { return nextLine().then(componentLike); }
    
    default C next(String text) { return nextLine().then(text); }
    
    default C nextUncolored(String text) { return nextLine().thenUncolored(text); }
    
    default C text(String text)
    {
        getBuilder().peekThenApply(child -> child.content(text));
        return self();
    }
    
    default C color(TextColor color)
    {
        Objects.requireNonNull(color, "color");
        getBuilder().peekThenApply(child -> child.color(color));
        return self();
    }
    
    default C color(RGBLike rgb)
    {
        Objects.requireNonNull(rgb, "rgb");
        return color(TextColor.color(rgb));
    }
    
    default C format(TextDecoration decoration)
    {
        Objects.requireNonNull(decoration, "decoration");
        getBuilder().peekThenApply(child -> child.decorate(decoration));
        return self();
    }
    
    default C format(TextDecoration ... decorations)
    {
        Objects.requireNonNull(decorations, "decorations");
        getBuilder().peekThenApply(child -> child.decorate(decorations));
        return self();
    }
    
    default C format(TextDecoration decoration, boolean state)
    {
        Objects.requireNonNull(decoration, "decoration");
        getBuilder().peekThenApply(child -> child.decoration(decoration, state));
        return self();
    }
    
    default C format(TextDecoration decoration, TextDecoration.State state)
    {
        Objects.requireNonNull(decoration, "decoration");
        Objects.requireNonNull(state, "state");
        getBuilder().peekThenApply(child -> child.decoration(decoration, state));
        return self();
    }
    
    default C bold() { return format(TextDecoration.BOLD); }
    
    default C bold(boolean state) { return format(TextDecoration.BOLD, state); }
    
    default C bold(TextDecoration.State state) { return format(TextDecoration.BOLD, state); }
    
    default C italic() { return format(TextDecoration.ITALIC); }
    
    default C italic(boolean state) { return format(TextDecoration.ITALIC, state); }
    
    default C italic(TextDecoration.State state) { return format(TextDecoration.ITALIC, state); }
    
    default C obfuscated() { return format(TextDecoration.OBFUSCATED); }
    
    default C obfuscated(boolean state) { return format(TextDecoration.OBFUSCATED, state); }
    
    default C obfuscated(TextDecoration.State state) { return format(TextDecoration.OBFUSCATED, state); }
    
    default C strikethrough() { return format(TextDecoration.STRIKETHROUGH); }
    
    default C strikethrough(boolean state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    default C strikethrough(TextDecoration.State state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    default C underlined() { return format(TextDecoration.UNDERLINED); }
    
    default C underlined(boolean state) { return format(TextDecoration.UNDERLINED, state); }
    
    default C underlined(TextDecoration.State state) { return format(TextDecoration.UNDERLINED, state); }
    
    default C click(ClickEvent event)
    {
        Objects.requireNonNull(event, "event");
        getBuilder().peekThenApply(child -> child.clickEvent(event));
        return self();
    }
    
    default C command(String command)
    {
        Objects.requireNonNull(command, "command");
        return click(ClickEvent.runCommand(command));
    }
    
    default C suggest(String suggestion)
    {
        Objects.requireNonNull(suggestion, "suggestion");
        return click(ClickEvent.suggestCommand(suggestion));
    }
    
    default C link(String link)
    {
        Objects.requireNonNull(link, "link");
        return click(ClickEvent.openUrl(link));
    }
    
    default C insertion(String insert)
    {
        Objects.requireNonNull(insert, "insert");
        getBuilder().peekThenApply(child -> child.insertion(insert));
        return self();
    }
    
    default C hover(HoverEventSource<?> eventSource)
    {
        Objects.requireNonNull(eventSource, "eventSource");
        getBuilder().peekThenApply(child -> child.hoverEvent(eventSource));
        return self();
    }
    
    default C tooltip(ComponentLike componentLike)
    {
        Objects.requireNonNull(componentLike, "componentLike");
        return hover(HoverEvent.showText(componentLike));
    }
    
    default C tooltip(Consumer<? super C> tooltipConsumer)
    {
        Objects.requireNonNull(tooltipConsumer, "tooltipConsumer");
        C tooltipChain = getConstructor().apply(new WrappedTextComponentBuilder(Component.text()));
        tooltipConsumer.accept(tooltipChain);
        return tooltip(tooltipChain);
    }
    
    default C tooltip(String tooltipString, ColorParsers colors)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(colors.parse(tooltipString));
    }
    
    default C tooltip(String tooltipString)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(tooltipString, ColorParsers.AMPERSAND);
    }
    
    default C tooltipUncolored(String tooltipString)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(tooltipString, ColorParsers.NONE);
    }
    
    default C send(Audience audience)
    {
        audience.sendMessage(this); // calls asComponent() -> stores result in case this is called multiple times.
        return self();
    }
    
    default C send(Audience audience, Identity source)
    {
        audience.sendMessage(source, this);
        return self();
    }
    
    default C send(Audience audience, Identified source)
    {
        audience.sendMessage(source, this);
        return self();
    }
    
    default C actionBar(Audience audience)
    {
        audience.sendActionBar(this);
        return self();
    }
}
