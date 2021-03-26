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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.RGBLike;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class Chain<C extends Chain<C>> implements AudienceSender<C>, ComponentLike
{
    private final WrappedTextComponentBuilder builder;
    
    public Chain(WrappedTextComponentBuilder builder)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
    }
    
    protected abstract ChainConstructor<C> getConstructor();
    
    protected abstract TextComponent processText(String text);
    
    @SuppressWarnings("unchecked")
    protected final C self() { return (C) this; }
    
    public final WrappedTextComponentBuilder getBuilder() { return builder; }
    
    @Override
    public final TextComponent asComponent() { return builder.asComponent(); }
    
    public final List<Component> asComponentList()
    {
        return TextChainSerializer.flattenComponentExtra(asComponent());
    }
    
    public final List<Component> asComponentListSplitByNewLine()
    {
        return TextChainSerializer.flattenComponentExtraSplitByNewLine(asComponent());
    }
    
    public C apply(Consumer<? super C> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(self());
        return self();
    }
    
    public <@NullOr M> M map(Function<? super C, M> mapper)
    {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply(self());
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
    
    public C then(ComponentLike componentLike)
    {
        Objects.requireNonNull(componentLike, "componentLike");
        Component component = Objects.requireNonNull(componentLike.asComponent(), "componentLike returned null");
        
        if (component instanceof TextComponent)
        {
            getBuilder().createNextChildWithBuilder(((TextComponent) component).toBuilder());
        }
        else
        {
            getBuilder().createNextChild().getComponentBuilder().append(component);
        }
        
        return self();
    }
    
    public C thenEmpty() { return then(Component.empty()); }
    
    public C then(String text, TextProcessor processor)
    {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(processor, "processor");
        return then(processor.apply(text));
    }
    
    public C then(String text) { return then(text, this::processText); }
    
    public C thenUnprocessed(String text) { return then(text, TextProcessor::none); }
    
    public C space() { return then(Component.space()); }
    
    public C nextLine() { return then(Component.newline()); }
    
    public C next(ComponentLike componentLike) { return nextLine().then(componentLike); }
    
    public C next(String text, TextProcessor processor)
    {
        return nextLine().then(text, processor);
    }
    
    public C next(String text) { return nextLine().then(text); }
    
    public C nextUnprocessed(String text) { return nextLine().thenUnprocessed(text); }
    
    public C text(String text)
    {
        Objects.requireNonNull(text, "text");
        getBuilder().peekThenApply(child -> child.content(text));
        return self();
    }
    
    public C style(Style style)
    {
        Objects.requireNonNull(style, "style");
        getBuilder().peekThenApply(child -> child.style(style));
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
    
    public C unformatted()
    {
        color(NamedTextColor.WHITE);
        bold(false);
        italic(false);
        obfuscated(false);
        strikethrough(false);
        underlined(false);
        return self();
    }
    
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
    
    public C insertion(String insertion)
    {
        Objects.requireNonNull(insertion, "insertion");
        getBuilder().peekThenApply(child -> child.insertion(insertion));
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
    
    public C tooltip(String tooltipText, TextProcessor processor)
    {
        Objects.requireNonNull(tooltipText, "tooltipText");
        Objects.requireNonNull(processor, "processor");
        return tooltip(processor.apply(tooltipText));
    }
    
    public C tooltip(String tooltipText) { return tooltip(tooltipText, this::processText); }
    
    public C tooltipUnprocessed(String tooltipText) { return tooltip(tooltipText, TextProcessor::none); }
    
    @Override
    public C send(Audience audience)
    {
        audience.sendMessage(this);
        return self();
    }
    
    @Override
    public C send(Audience audience, Identity source)
    {
        audience.sendMessage(source, this);
        return self();
    }
    
    @Override
    public C send(Audience audience, Identified source)
    {
        audience.sendMessage(source, this);
        return self();
    }
    
    @Override
    public C actionBar(Audience audience)
    {
        audience.sendActionBar(this);
        return self();
    }
}
