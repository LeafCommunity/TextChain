/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.adventure.TextChainSource;
import net.kyori.adventure.platform.AudienceProvider;

public interface PlatformTextChainSource<A extends AudienceProvider, T extends TextChain<T>>
    extends AdventureSource<A>, TextChainSource<T> {}
