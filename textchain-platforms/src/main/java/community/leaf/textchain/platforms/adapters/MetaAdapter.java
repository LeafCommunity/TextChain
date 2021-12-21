/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("NullableProblems") // no problems here
public interface MetaAdapter<M>
{
	Optional<Component> displayName(@NullOr M meta);
	
	@NullOr M displayName(@NullOr M meta, ComponentLike componentLike);
	
	List<Component> lore(@NullOr M meta);
	
	@NullOr M lore(@NullOr M meta, List<Component> lore);
	
	@NullOr M lore(@NullOr M meta, ComponentLike componentLike);
}
