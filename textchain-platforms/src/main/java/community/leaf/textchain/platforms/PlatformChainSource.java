/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainSource;
import net.kyori.adventure.platform.AudienceProvider;

public interface PlatformChainSource<A extends AudienceProvider, C extends Chain<C>>
    extends AdventureSource<A>, ChainSource<C> {}
