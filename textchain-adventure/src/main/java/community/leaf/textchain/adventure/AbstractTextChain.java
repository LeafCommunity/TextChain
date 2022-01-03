/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import java.util.Objects;

public abstract class AbstractTextChain<T extends TextChain<T>> implements TextChain<T>
{
    private final LinearTextComponentBuilder builder;
    private final TextProcessor processor;
    
    protected AbstractTextChain(LinearTextComponentBuilder builder, TextProcessor processor)
    {
        this.builder = Objects.requireNonNull(builder, "builder");
        this.processor = Objects.requireNonNull(processor, "processor");
    }
    
    @Override
    public final LinearTextComponentBuilder builder() { return builder; }
    
    @Override
    public final TextProcessor processor() { return processor; }
    
    static final class Impl extends AbstractTextChain<Impl>
    {
        static final TextChainSource<Impl> SOURCE = () -> Impl::new;
        
        Impl(LinearTextComponentBuilder builder, TextProcessor processor) { super(builder, processor); }
        
        @Override
        public TextChainConstructor<Impl> constructor() { return Impl::new; }
    }
}
