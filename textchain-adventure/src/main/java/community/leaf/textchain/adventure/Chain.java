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
public abstract class Chain<C extends Chain<C>> implements ComponentLike
{
    private final WrappedTextComponentBuilder builder;
    
    public Chain(WrappedTextComponentBuilder builder)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
    }
    
    protected abstract TextChainConstructor<C> getConstructor();
    
    public final WrappedTextComponentBuilder getBuilder() { return builder; }
    
    @Override
    public final TextComponent asComponent() { return getBuilder().asComponent(); }
    
    @SuppressWarnings("unchecked")
    protected final C self() { return (C) this; }
    
    public C apply(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(self());
        return self();
    }
    
    public C extra(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(getBuilder().peekOrCreateChild().wrap(getConstructor()));
        return self();
    }
    
    public C thenExtra(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(getBuilder().createNextChild().wrap(getConstructor()));
        return self();
    }
    
    public C then(TextComponent.Builder builder)
    {
        Objects.requireNonNull(builder, "builder");
        getBuilder().createNextChildWithBuilder(builder);
        return self();
    }
    
    public C then(ComponentLike componentLike)
    {
        Objects.requireNonNull(componentLike, "componentLike");
        Component component = Objects.requireNonNull(componentLike.asComponent(), "componentLike returned null");
        if (component instanceof TextComponent) { return then(((TextComponent) component).toBuilder()); }
        getBuilder().createNextChild().getComponentBuilder().append(component);
        return self();
    }
    
    public C then(String text, ColorParsers colors)
    {
        Objects.requireNonNull(text, "text");
        return then(colors.parse(text).toBuilder());
    }
    
    public C then(String text) { return then(text, ColorParsers.AMPERSAND); }
    
    public C thenUncolored(String text) { return then(text, ColorParsers.NONE); }
    
    public C nextLine() { return thenUncolored("\n"); }
    
    public C next(TextComponent.Builder builder) { return nextLine().then(builder); }
    
    public C next(ComponentLike componentLike) { return nextLine().then(componentLike); }
    
    public C next(String text) { return nextLine().then(text); }
    
    public C nextUncolored(String text) { return nextLine().thenUncolored(text); }
    
    public C text(String text)
    {
        getBuilder().peekThenApply(child -> child.content(text));
        return self();
    }
    
    public C color(TextColor color)
    {
        Objects.requireNonNull(color, "color");
        getBuilder().peekThenApply(child -> child.color(color));
        return self();
    }
    
    public C color(RGBLike rgb)
    {
        Objects.requireNonNull(rgb, "rgb");
        return color(TextColor.color(rgb));
    }
    
    public C format(TextDecoration decoration)
    {
        Objects.requireNonNull(decoration, "decoration");
        getBuilder().peekThenApply(child -> child.decorate(decoration));
        return self();
    }
    
    public C format(TextDecoration ... decorations)
    {
        Objects.requireNonNull(decorations, "decorations");
        getBuilder().peekThenApply(child -> child.decorate(decorations));
        return self();
    }
    
    public C format(TextDecoration decoration, boolean state)
    {
        Objects.requireNonNull(decoration, "decoration");
        getBuilder().peekThenApply(child -> child.decoration(decoration, state));
        return self();
    }
    
    public C format(TextDecoration decoration, TextDecoration.State state)
    {
        Objects.requireNonNull(decoration, "decoration");
        Objects.requireNonNull(state, "state");
        getBuilder().peekThenApply(child -> child.decoration(decoration, state));
        return self();
    }
    
    public C bold() { return format(TextDecoration.BOLD); }
    
    public C bold(boolean state) { return format(TextDecoration.BOLD, state); }
    
    public C bold(TextDecoration.State state) { return format(TextDecoration.BOLD, state); }
    
    public C italic() { return format(TextDecoration.ITALIC); }
    
    public C italic(boolean state) { return format(TextDecoration.ITALIC, state); }
    
    public C italic(TextDecoration.State state) { return format(TextDecoration.ITALIC, state); }
    
    public C obfuscated() { return format(TextDecoration.OBFUSCATED); }
    
    public C obfuscated(boolean state) { return format(TextDecoration.OBFUSCATED, state); }
    
    public C obfuscated(TextDecoration.State state) { return format(TextDecoration.OBFUSCATED, state); }
    
    public C strikethrough() { return format(TextDecoration.STRIKETHROUGH); }
    
    public C strikethrough(boolean state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    public C strikethrough(TextDecoration.State state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    public C underlined() { return format(TextDecoration.UNDERLINED); }
    
    public C underlined(boolean state) { return format(TextDecoration.UNDERLINED, state); }
    
    public C underlined(TextDecoration.State state) { return format(TextDecoration.UNDERLINED, state); }
    
    public C click(ClickEvent event)
    {
        Objects.requireNonNull(event, "event");
        getBuilder().peekThenApply(child -> child.clickEvent(event));
        return self();
    }
    
    public C command(String command)
    {
        Objects.requireNonNull(command, "command");
        return click(ClickEvent.runCommand(command));
    }
    
    public C suggest(String suggestion)
    {
        Objects.requireNonNull(suggestion, "suggestion");
        return click(ClickEvent.suggestCommand(suggestion));
    }
    
    public C link(String link)
    {
        Objects.requireNonNull(link, "link");
        return click(ClickEvent.openUrl(link));
    }
    
    public C insertion(String insert)
    {
        Objects.requireNonNull(insert, "insert");
        getBuilder().peekThenApply(child -> child.insertion(insert));
        return self();
    }
    
    public C hover(HoverEventSource<?> eventSource)
    {
        Objects.requireNonNull(eventSource, "eventSource");
        getBuilder().peekThenApply(child -> child.hoverEvent(eventSource));
        return self();
    }
    
    public C tooltip(ComponentLike componentLike)
    {
        Objects.requireNonNull(componentLike, "componentLike");
        return hover(HoverEvent.showText(componentLike));
    }
    
    public C tooltip(Consumer<? super C> tooltipConsumer)
    {
        Objects.requireNonNull(tooltipConsumer, "tooltipConsumer");
        C tooltipChain = getConstructor().apply(new WrappedTextComponentBuilder(Component.text()));
        tooltipConsumer.accept(tooltipChain);
        return tooltip(tooltipChain);
    }
    
    public C tooltip(String tooltipString, ColorParsers colors)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(colors.parse(tooltipString));
    }
    
    public C tooltip(String tooltipString)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(tooltipString, ColorParsers.AMPERSAND);
    }
    
    public C tooltipUncolored(String tooltipString)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(tooltipString, ColorParsers.NONE);
    }
    
    public C send(Audience audience)
    {
        audience.sendMessage(this); // calls asComponent() -> stores result in case this is called multiple times.
        return self();
    }
    
    public C send(Audience audience, Identity source)
    {
        audience.sendMessage(source, this);
        return self();
    }
    
    public C send(Audience audience, Identified source)
    {
        audience.sendMessage(source, this);
        return self();
    }
    
    public C actionBar(Audience audience)
    {
        audience.sendActionBar(this);
        return self();
    }
}
