package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("UnusedReturnValue")
public class WrappedTextComponentBuilder implements ComponentLike
{
    private final Deque<WrappedTextComponentBuilder> children = new LinkedList<>();
    
    private final TextComponent.Builder builder;
    
    private @NullOr TextComponent result = null;
    
    public WrappedTextComponentBuilder(TextComponent.Builder builder)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
    }
    
    public TextComponent.Builder getComponentBuilder() { return builder; }
    
    public <W> W wrap(Function<WrappedTextComponentBuilder, W> wrapper)
    {
        return wrapper.apply(this);
    }
    
    @Override
    public TextComponent asComponent()
    {
        // Store the result to avoid constantly rebuilding the component.
        return (result != null) ? result : (result = aggregateThenRebuildComponent());
    }
    
    // This method will *always* rebuild the component (and child components).
    public TextComponent aggregateThenRebuildComponent()
    {
        result = null; // Invalidate existing result since it is being rebuilt (guarantees fresh results of children).
        if (children.isEmpty()) { return builder.build(); } // No children - simply build the builder.
        
        // Create a copy of the builder in order to avoid editing the mutable builder instance within this wrapper.
        TextComponent.Builder aggregate = builder.build().toBuilder();
        for (WrappedTextComponentBuilder child : children) { aggregate.append(child.aggregateThenRebuildComponent()); }
        return aggregate.build();
    }
    
    public WrappedTextComponentBuilder createNextChild()
    {
        result = null;
        WrappedTextComponentBuilder child = new WrappedTextComponentBuilder(Component.text());
        children.add(child);
        return child;
    }
    
    public WrappedTextComponentBuilder createNextChildWithBuilder(TextComponent.Builder builder)
    {
        result = null;
        WrappedTextComponentBuilder child = new WrappedTextComponentBuilder(builder);
        children.add(child);
        return child;
    }
    
    public WrappedTextComponentBuilder peekOrCreateChild()
    {
        result = null;
        return (children.isEmpty()) ? createNextChild() : children.getLast();
    }
    
    public void peekThenApply(Consumer<TextComponent.Builder> action)
    {
        result = null;
        action.accept(peekOrCreateChild().builder);
    }
}
