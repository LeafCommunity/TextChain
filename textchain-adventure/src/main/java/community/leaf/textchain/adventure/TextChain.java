package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;

public class TextChain implements ComponentLike
{
    public static TextChain empty() { return new TextChain(); }
    
    public static TextChain of(String text) { return new TextChain(text); }
    
    private final Deque<TextChain> children = new LinkedList<>();
    private final TextComponent.Builder builder;
    
    public TextChain(TextComponent.Builder builder) { this.builder = Objects.requireNonNull(builder, "builder"); }
    
    public TextChain() { this(Component.text()); }
    
    public TextChain(String text)
    {
        this();
        then(text);
    }
    
    public TextChain(TextComponent component) { this(component.toBuilder()); }
    
    @Override
    public @NonNull TextComponent asComponent()
    {
        if (children.isEmpty()) { return builder.build(); }
        
        TextComponent.Builder aggregate = builder.build().toBuilder();
        for (TextChain child : children) { aggregate.append(child.asComponent()); }
        return aggregate.build();
    }
    
    protected void createNextChildWithBuilder(TextComponent.Builder builder)
    {
        children.add(new TextChain(builder));
    }
    
    protected TextChain createNextChild()
    {
        TextChain child = new TextChain();
        children.add(child);
        return child;
    }
    
    protected TextChain peekOrCreateChild()
    {
        return (children.isEmpty()) ? createNextChild() : children.getLast();
    }
    
    protected void peekThenApply(Consumer<TextComponent.Builder> action)
    {
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
    
    public TextChain then(TextComponent.Builder builder)
    {
        Objects.requireNonNull(builder, "builder");
        createNextChildWithBuilder(builder);
        return this;
    }
    
    public TextChain then(TextComponent component)
    {
        Objects.requireNonNull(component, "component");
        return then(component.toBuilder());
    }
    
    public TextChain then(TextChain other)
    {
        Objects.requireNonNull(other, "other");
        return then(other.asComponent());
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
    
    public TextChain next(TextComponent component) { return nextLine().then(component); }
    
    public TextChain next(TextChain other) { return nextLine().then(other); }
    
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
    
    public TextChain format(TextDecoration ... decorations)
    {
        Objects.requireNonNull(decorations, "decorations");
        peekThenApply(child -> child.decorate(decorations));
        return this;
    }
    
    public TextChain bold()
    {
        peekThenApply(child -> child.decorate(TextDecoration.BOLD));
        return this;
    }
    
    public TextChain italic()
    {
        peekThenApply(child -> child.decorate(TextDecoration.ITALIC));
        return this;
    }
    
    public TextChain magic()
    {
        peekThenApply(child -> child.decorate(TextDecoration.OBFUSCATED));
        return this;
    }
    
    public TextChain strikethrough()
    {
        peekThenApply(child -> child.decorate(TextDecoration.STRIKETHROUGH));
        return this;
    }
    
    public TextChain underline()
    {
        peekThenApply(child -> child.decorate(TextDecoration.UNDERLINED));
        return this;
    }
    
    public TextChain command(String command)
    {
        Objects.requireNonNull(command, "command");
        peekThenApply(child -> child.clickEvent(ClickEvent.runCommand(command)));
        return this;
    }
    
    public TextChain suggest(String suggestion)
    {
        Objects.requireNonNull(suggestion, "suggestion");
        peekThenApply(child -> child.clickEvent(ClickEvent.suggestCommand(suggestion)));
        return this;
    }
    
    public TextChain link(String link)
    {
        Objects.requireNonNull(link, "link");
        peekThenApply(child -> child.clickEvent(ClickEvent.openUrl(link)));
        return this;
    }
    
    
    public TextChain insertion(String insert)
    {
        Objects.requireNonNull(insert, "insert");
        peekThenApply(child -> child.insertion(insert));
        return this;
    }
    
    public TextChain tooltip(Component tooltipComponent)
    {
        Objects.requireNonNull(tooltipComponent, "tooltipComponent");
        peekThenApply(child -> child.hoverEvent(HoverEvent.showText(tooltipComponent)));
        return this;
    }
    
    public TextChain tooltip(TextChain tooltipTextChain)
    {
        Objects.requireNonNull(tooltipTextChain, "tooltipTellable");
        return tooltip(tooltipTextChain.asComponent());
    }
    
    public TextChain tooltip(String tooltipString)
    {
        Objects.requireNonNull(tooltipString, "tooltipString");
        return tooltip(LegacyComponentSerializer.legacyAmpersand().deserialize(tooltipString));
    }
}
