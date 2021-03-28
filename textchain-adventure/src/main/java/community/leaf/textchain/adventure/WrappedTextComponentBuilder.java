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

/**
 * Contains text component builders for modification
 * and eventual component generation and caching.
 */
@SuppressWarnings("UnusedReturnValue")
public class WrappedTextComponentBuilder implements ComponentLike
{
    private final Deque<WrappedTextComponentBuilder> children = new LinkedList<>();
    
    private final TextComponent.Builder builder;
    
    private @NullOr TextComponent result = null;
    
    /**
     * Wraps an existing Adventure text component builder.
     *
     * @param builder   an unwrapped text component builder
     */
    public WrappedTextComponentBuilder(TextComponent.Builder builder)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
    }
    
    /**
     * Gets the internal Adventure text component builder
     * contained within this wrapper.
     *
     * @return  the internal text component builder
     */
    public TextComponent.Builder getComponentBuilder() { return builder; }
    
    /**
     * Wrap this wrapper into something else.
     * Applies the wrapper function with {@code this}
     * instance and returns the result.
     *
     * @param wrapper   wrapper function
     * @param <W>   wrapper type
     * @return  result of applying the wrapper function
     */
    public <W> W wrap(Function<WrappedTextComponentBuilder, W> wrapper)
    {
        return wrapper.apply(this);
    }
    
    /**
     * Generates or gets the cached component resulting
     * from aggregating all internal builders.
     *
     * @return  the aggregate built component
     *
     * @see #aggregateThenRebuildComponent()
     */
    @Override
    public TextComponent asComponent()
    {
        // Store the result to avoid constantly rebuilding the component.
        return (result != null) ? result : (result = aggregateThenRebuildComponent());
    }
    
    /**
     * Generate a new component by cloning and
     * appending all child builders.
     *
     * <p><b>Note:</b> this method will <i>always</i>
     * rebuild the component and all of its children,
     * and invalidate all associated cached results.</p>
     *
     * @return  an aggregate built component
     */
    public TextComponent aggregateThenRebuildComponent()
    {
        result = null; // Invalidate existing result since it is being rebuilt (guarantees fresh results of children).
        if (children.isEmpty()) { return builder.build(); } // No children - simply build the builder.
        
        // Create a copy of the builder in order to avoid editing the mutable builder instance within this wrapper.
        TextComponent.Builder aggregate = builder.build().toBuilder();
        for (WrappedTextComponentBuilder child : children) { aggregate.append(child.aggregateThenRebuildComponent()); }
        return aggregate.build();
    }
    
    /**
     * Creates a new child wrapper containing
     * the provided text component builder
     * and returns it.
     *
     * <p><b>Note:</b> this method will invalidate
     * existing cached results since the builder's
     * state will mutate upon calling this.</p>
     *
     * @param builder   the child's internal builder
     * @return  a new child containing the provided builder
     */
    public WrappedTextComponentBuilder createNextChildWithBuilder(TextComponent.Builder builder)
    {
        result = null;
        WrappedTextComponentBuilder child = new WrappedTextComponentBuilder(builder);
        children.add(child);
        return child;
    }
    
    /**
     * Creates a new child wrapper containing
     * a new empty text component builder
     * and returns it.
     *
     * <p><b>Note:</b> this method will invalidate
     * existing cached results since the builder's
     * state will mutate upon calling this.</p>
     *
     * @return  a new child
     */
    public WrappedTextComponentBuilder createNextChild()
    {
        // result invalidated in createNextChildWithBuilder()
        return createNextChildWithBuilder(Component.text());
    }
    
    /**
     * Gets the latest child or creates
     * one if none exist and returns it.
     *
     * <p><b>Note:</b> this method will invalidate
     * existing cached results since the builder's
     * state will mutate upon calling this.</p>
     *
     * @return  the latest child
     */
    public WrappedTextComponentBuilder peekOrCreateChild()
    {
        result = null;
        return (children.isEmpty()) ? createNextChild() : children.getLast();
    }
    
    /**
     * Gets the latest child or creates one
     * if none exist then performs the provided
     * action on its internal text component
     * builder.
     *
     * <p><b>Note:</b> this method will invalidate
     * existing cached results since the builder's
     * state will mutate upon calling this.</p>
     *
     * @param action    the action to perform on the
     *                  child's internal builder
     */
    public void peekThenApply(Consumer<TextComponent.Builder> action)
    {
        // result invalidated in peekOrCreateChild()
        action.accept(peekOrCreateChild().getComponentBuilder());
    }
}
