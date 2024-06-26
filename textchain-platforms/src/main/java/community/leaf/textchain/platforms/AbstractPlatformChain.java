/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms;

import community.leaf.textchain.adventure.AbstractChain;
import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.LinearTextComponentBuilder;
import community.leaf.textchain.adventure.TextProcessor;
import net.kyori.adventure.platform.AudienceProvider;

import java.util.Objects;

public abstract class AbstractPlatformChain<A extends AudienceProvider, C extends Chain<C>> extends AbstractChain<C> implements AdventureSource<A>
{
    private final A audiences;
    
    protected AbstractPlatformChain(LinearTextComponentBuilder builder, TextProcessor processor, A audiences)
    {
        super(builder, processor);
        this.audiences = Objects.requireNonNull(audiences, "audiences");
    }
    
    @Override
    public A adventure()
    {
        return audiences;
    }
}
