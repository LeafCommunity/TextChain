/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

/**
 * Generates standard chain constructors.
 *
 * @param <T>   chain type
 */
@FunctionalInterface
public interface TextChainSource<T extends TextChain<T>>
{
    /**
     * Generates a standard chain constructor by supplying all other dependencies the specific
     * chain type requires.
     *
     * @return a standard chain constructor
     */
    TextChainConstructor<T> textChainConstructor();
}
