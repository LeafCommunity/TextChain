/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.adapters;

import community.leaf.textchain.platforms.adapters.EntityTypeAdapter;
import community.leaf.textchain.platforms.bukkit.internal.nms.EntityReflection;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;

class BukkitEntityTypeAdapter implements EntityTypeAdapter<EntityType>
{
	private final BukkitKeyAdapter keys;
	
	public BukkitEntityTypeAdapter(BukkitKeyAdapter keys) { this.keys = keys; }
	
	@Override
	public Key key(EntityType type)
	{
		return keys.key(type.getKey());
	}
	
	@Override
	public String translationKey(EntityType type)
	{
		try { return EntityReflection.entities().translationKey(type); }
		catch (Throwable throwable) { throw new RuntimeException(throwable); }
	}
}
