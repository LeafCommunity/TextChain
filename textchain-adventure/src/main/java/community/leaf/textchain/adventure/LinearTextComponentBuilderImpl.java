/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
final class LinearTextComponentBuilderImpl implements LinearTextComponentBuilder
{
    private final Deque<LinearTextComponentBuilderImpl> children = new ArrayDeque<>();
    
    private final TextComponent.Builder builder;
    
    private @NullOr TextComponent result = null;
    
    LinearTextComponentBuilderImpl(TextComponent.Builder builder)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
    }
    
    @Override
    public TextComponent.Builder wrappedComponentBuilder() { return builder; }
    
    @Override
    public TextComponent asComponent()
    {
        // Store the result to avoid constantly rebuilding the component.
        return (result != null) ? result : (result = aggregateThenRebuildComponent());
    }
    
    @Override
    public TextComponent aggregateThenRebuildComponent()
    {
        result = null; // Invalidate existing result since it is being rebuilt (guarantees fresh results of children).
        if (children.isEmpty()) { return builder.build(); } // No children - simply build the builder.
        
        // Create a copy of the builder in order to avoid editing the mutable builder instance within this wrapper.
        TextComponent.Builder aggregate = builder.build().toBuilder();
        for (LinearTextComponentBuilderImpl child : children) { aggregate.append(child.aggregateThenRebuildComponent()); }
        return aggregate.build();
    }
    
    @Override
    public LinearTextComponentBuilder createNextChildWithBuilder(TextComponent.Builder builder)
    {
        result = null;
        LinearTextComponentBuilderImpl child = new LinearTextComponentBuilderImpl(builder);
        children.add(child);
        return child;
    }
    
    @Override
    public LinearTextComponentBuilder createNextChild()
    {
        // result invalidated in createNextChildWithBuilder()
        return createNextChildWithBuilder(Component.text());
    }
    
    @Override
    public LinearTextComponentBuilder peekOrCreateChild()
    {
        result = null;
        return (children.isEmpty()) ? createNextChild() : children.getLast();
    }
    
    @Override
    public void peekThenApply(Consumer<TextComponent.Builder> action)
    {
        // result invalidated in peekOrCreateChild()
        action.accept(peekOrCreateChild().wrappedComponentBuilder());
    }
}
