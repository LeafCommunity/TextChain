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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.util.RGBLike;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class TextChain implements ComponentLike
{
    public static TextChain empty() { return new TextChain(); }
    
    public static TextChain of(String text) { return new TextChain().then(text); }
    
    public static TextChain of(ComponentLike componentLike) { return new TextChain().then(componentLike); }
    
    private static <T> T chain(T thing, Consumer<T> consumer)
    {
        consumer.accept(thing);
        return thing;
    }
    
    protected final Deque<TextChain> children = new LinkedList<>();
    protected final TextComponent.Builder builder;
    protected @NullOr TextComponent result = null;
    
    public TextChain(TextComponent.Builder builder) { this.builder = Objects.requireNonNull(builder, "builder"); }
    
    public TextChain() { this(Component.text()); }
    
    public TextChain(TextComponent component) { this(component.toBuilder()); }
    
    @Override
    public TextComponent asComponent()
    {
        // Store the result to avoid constantly rebuilding the component.
        return (result != null) ? result : (result = aggregateThenBuildComponent());
    }
    
    // This method will *always* rebuild the component (and child components).
    protected TextComponent aggregateThenBuildComponent()
    {
        result = null; // Invalidate existing result since it is being rebuilt (guarantees fresh results of children).
        if (children.isEmpty()) { return builder.build(); } // No children - simply build the builder.
        
        // Create a copy of the builder in order to avoid editing the mutable builder instance within this TextChain.
        TextComponent.Builder aggregate = builder.build().toBuilder();
        for (TextChain child : children) { aggregate.append(child.aggregateThenBuildComponent()); }
        return aggregate.build();
    }
    
    protected TextChain createNextChild()
    {
        result = null;
        TextChain child = new TextChain();
        children.add(child);
        return child;
    }
    
    protected TextChain createNextChildWithBuilder(TextComponent.Builder builder)
    {
        result = null;
        TextChain child = new TextChain(builder);
        children.add(child);
        return child;
    }
    
    protected TextChain peekOrCreateChild()
    {
        result = null;
        return (children.isEmpty()) ? createNextChild() : children.getLast();
    }
    
    protected void peekThenApply(Consumer<TextComponent.Builder> action)
    {
        result = null;
        action.accept(peekOrCreateChild().builder);
    }
    
    public TextChain apply(Consumer<TextChain> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(this);
        return this;
    }
    
    public TextChain extra(Consumer<TextChain> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(peekOrCreateChild());
        return this;
    }
    
    public TextChain thenExtra(Consumer<TextChain> consumer)
    {
        Objects.requireNonNull(consumer, "consumer");
        consumer.accept(createNextChild());
        return this;
    }
    
    public TextChain then(TextComponent.Builder builder)
    {
        Objects.requireNonNull(builder, "builder");
        createNextChildWithBuilder(builder);
        return this;
    }
    
    public TextChain then(ComponentLike componentLike)
    {
        Objects.requireNonNull(componentLike, "componentLike");
        Component component = Objects.requireNonNull(componentLike.asComponent(), "componentLike returned null");
        if (component instanceof TextComponent) { return then(((TextComponent) component).toBuilder()); }
        createNextChild().builder.append(component);
        return this;
    }
    
    public TextChain then(String text, boolean parseLegacyColors)
    {
        Objects.requireNonNull(text, "text");
        return then(
            (parseLegacyColors)
                ? LegacyComponentSerializer.legacyAmpersand().deserialize(text).toBuilder()
                : Component.text().content(text)
        );
    }
    
    public TextChain then(String text) { return then(text, true); }
    
    public TextChain thenUncolored(String text) { return then(text, false); }
    
    public TextChain nextLine() { return thenUncolored("\n"); }
    
    public TextChain next(TextComponent.Builder builder) { return nextLine().then(builder); }
    
    public TextChain next(ComponentLike componentLike) { return nextLine().then(componentLike); }
    
    public TextChain next(String text) { return nextLine().then(text); }
    
    public TextChain nextUncolored(String text) { return nextLine().thenUncolored(text); }
    
    public TextChain text(String text)
    {
        peekThenApply(child -> child.content(text));
        return this;
    }
    
    public TextChain color(TextColor color)
    {
        Objects.requireNonNull(color, "color");
        peekThenApply(child -> child.color(color));
        return this;
    }
    
    public TextChain color(RGBLike rgb)
    {
        Objects.requireNonNull(rgb, "rgb");
        return color(TextColor.color(rgb));
    }
    
    public TextChain format(TextDecoration decoration)
    {
        Objects.requireNonNull(decoration, "decoration");
        peekThenApply(child -> child.decorate(decoration));
        return this;
    }
    
    public TextChain format(TextDecoration ... decorations)
    {
        Objects.requireNonNull(decorations, "decorations");
        peekThenApply(child -> child.decorate(decorations));
        return this;
    }
    
    public TextChain format(TextDecoration decoration, boolean state)
    {
        Objects.requireNonNull(decoration, "decoration");
        peekThenApply(child -> child.decoration(decoration, state));
        return this;
    }
    
    public TextChain format(TextDecoration decoration, TextDecoration.State state)
    {
        Objects.requireNonNull(decoration, "decoration");
        Objects.requireNonNull(state, "state");
        peekThenApply(child -> child.decoration(decoration, state));
        return this;
    }
    
    public TextChain bold() { return format(TextDecoration.BOLD); }
    
    public TextChain bold(boolean state) { return format(TextDecoration.BOLD, state); }
    
    public TextChain bold(TextDecoration.State state) { return format(TextDecoration.BOLD, state); }
    
    public TextChain italic() { return format(TextDecoration.ITALIC); }
    
    public TextChain italic(boolean state) { return format(TextDecoration.ITALIC, state); }
    
    public TextChain italic(TextDecoration.State state) { return format(TextDecoration.ITALIC, state); }
    
    public TextChain obfuscated() { return format(TextDecoration.OBFUSCATED); }
    
    public TextChain obfuscated(boolean state) { return format(TextDecoration.OBFUSCATED, state); }
    
    public TextChain obfuscated(TextDecoration.State state) { return format(TextDecoration.OBFUSCATED, state); }
    
    public TextChain strikethrough() { return format(TextDecoration.STRIKETHROUGH); }
    
    public TextChain strikethrough(boolean state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    public TextChain strikethrough(TextDecoration.State state) { return format(TextDecoration.STRIKETHROUGH, state); }
    
    public TextChain underlined() { return format(TextDecoration.UNDERLINED); }
    
    public TextChain underlined(boolean state) { return format(TextDecoration.UNDERLINED, state); }
    
    public TextChain underlined(TextDecoration.State state) { return format(TextDecoration.UNDERLINED, state); }
    
    public TextChain click(ClickEvent event)
    {
        Objects.requireNonNull(event, "event");
        peekThenApply(child -> child.clickEvent(event));
        return this;
    }
    
    public TextChain command(String command)
    {
        Objects.requireNonNull(command, "command");
        return click(ClickEvent.runCommand(command));
    }
    
    public TextChain suggest(String suggestion)
    {
        Objects.requireNonNull(suggestion, "suggestion");
        return click(ClickEvent.suggestCommand(suggestion));
    }
    
    public TextChain link(String link)
    {
        Objects.requireNonNull(link, "link");
        return click(ClickEvent.openUrl(link));
    }
    
    public TextChain insertion(String insert)
    {
        Objects.requireNonNull(insert, "insert");
        peekThenApply(child -> child.insertion(insert));
        return this;
    }
    
    public TextChain hover(HoverEventSource<?> eventSource)
    {
        Objects.requireNonNull(eventSource, "eventSource");
        peekThenApply(child -> child.hoverEvent(eventSource));
        return this;
    }
    
    public TextChain tooltip(Component tooltipComponent)
    {
        Objects.requireNonNull(tooltipComponent, "tooltipComponent");
        return hover(HoverEvent.showText(tooltipComponent));
    }
    
    public TextChain tooltip(TextChain tooltipTextChain)
    {
        Objects.requireNonNull(tooltipTextChain, "tooltipTextChain");
        return tooltip(tooltipTextChain.asComponent());
    }
    
    public TextChain tooltip(String tooltipString)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(LegacyComponentSerializer.legacyAmpersand().deserialize(tooltipString));
    }
    
    public TextChain tooltip(Consumer<TextChain> tooltipConsumer)
    {
        Objects.requireNonNull(tooltipConsumer, "tooltipConsumer");
        return tooltip(chain(TextChain.empty(), tooltipConsumer));
    }
    
    public TextChain send(Audience audience)
    {
        audience.sendMessage(this); // calls asComponent() -> stores result in case this is called multiple times.
        return this;
    }
    
    public TextChain send(Audience audience, Identity source)
    {
        audience.sendMessage(source, this);
        return this;
    }
    
    public TextChain send(Audience audience, Identified source)
    {
        audience.sendMessage(source, this);
        return this;
    }
    
    public TextChain actionBar(Audience audience)
    {
        audience.sendActionBar(this);
        return this;
    }
}
