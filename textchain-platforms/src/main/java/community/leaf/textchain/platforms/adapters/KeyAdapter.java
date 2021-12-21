/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

public interface KeyAdapter<K, H>
{
	Key key(K key);
	
	Keyed keyed(H keyed);
}
