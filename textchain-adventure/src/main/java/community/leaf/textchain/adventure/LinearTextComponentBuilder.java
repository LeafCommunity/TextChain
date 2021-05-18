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

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Contains text component builders for modification
 * and eventual component generation and caching.
 */
public interface LinearTextComponentBuilder extends ComponentLike
{
    static LinearTextComponentBuilder empty()
    {
        return new LinearTextComponentBuilderImpl(Component.text());
    }
    
    /**
     * Wraps an existing Adventure text component builder.
     *
     * @param existing  an unwrapped text component builder
     *
     * @return  a linear text component builder containing
     *          the provided builder
     */
    static LinearTextComponentBuilder wrap(TextComponent.Builder existing)
    {
        return new LinearTextComponentBuilderImpl(existing);
    }
    
    /**
     * Gets the internal Adventure text component builder
     * contained within this wrapper.
     *
     * @return  the internal text component builder
     */
    TextComponent.Builder getComponentBuilder();
    
    /**
     * Generates or gets the cached component resulting
     * from aggregating all internal builders.
     *
     * @return  the aggregate built component
     *
     * @see #aggregateThenRebuildComponent()
     */
    @Override
    TextComponent asComponent();
    
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
    TextComponent aggregateThenRebuildComponent();
    
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
    LinearTextComponentBuilder createNextChildWithBuilder(TextComponent.Builder builder);
    
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
    LinearTextComponentBuilder createNextChild();
    
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
    LinearTextComponentBuilder peekOrCreateChild();
    
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
    void peekThenApply(Consumer<TextComponent.Builder> action);
    
    /**
     * Puts this wrapped builder into something else.
     * Applies the wrapper function with {@code this}
     * instance and returns the result.
     *
     * @param wrapper   wrapper function
     * @param <W>   wrapper type
     * @return  result of applying the wrapper function
     */
    default  <W> W into(Function<LinearTextComponentBuilder, W> wrapper)
    {
        return wrapper.apply(this);
    }
}
