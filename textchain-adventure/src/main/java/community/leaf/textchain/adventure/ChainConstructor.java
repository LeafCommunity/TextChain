/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

/**
 * Standard chain constructor: takes a
 * {@link LinearTextComponentBuilder}
 * and creates a new chain instance.
 *
 * @param <C>   chain type
 */
@FunctionalInterface
public interface ChainConstructor<C extends Chain<C>>
{
    C construct(LinearTextComponentBuilder builder, TextProcessor processor);
}
