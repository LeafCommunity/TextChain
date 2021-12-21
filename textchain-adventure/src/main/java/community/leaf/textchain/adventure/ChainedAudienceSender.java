/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

/**
 * Sends components to an audience.
 *
 * @param <S>   self-returning subtype
 *              (for method chaining)
 */
public interface ChainedAudienceSender<S extends ChainedAudienceSender<S>> extends ComponentLike
{
	@SuppressWarnings("unchecked")
	private S self() { return (S) this; }
	
	/**
	 * Converts into a component message and sends
	 * it to the provided audience.
	 *
	 * @param audience  the audience to receive components
	 * @return  self (for method chaining)
	 *
	 * @see Audience#sendMessage(Component)
	 */
	default S sendToAudience(Audience audience)
	{
		audience.sendMessage(asComponent());
		return self();
	}
	
	/**
	 * Converts into a component message and sends
	 * it to the provided audience, identified by
	 * the provided identity.
	 *
	 * @param audience  the audience to receive components
	 * @param source    the identity from whom the
	 *                  components originate
	 * @return  self (for method chaining)
	 *
	 * @see Audience#sendMessage(Identity, Component)
	 */
	default S sendToAudience(Audience audience, Identity source)
	{
		audience.sendMessage(source, asComponent());
		return self();
	}
	
	/**
	 * Converts into a component message and sends
	 * it to the provided audience, identified by
	 * the source's identity.
	 *
	 * @param audience  the audience to receive components
	 * @param source    the identifiable source from whom
	 *                  the components originate
	 * @return  self (for method chaining)
	 *
	 * @see Audience#sendMessage(Identified, Component)
	 */
	default S sendToAudience(Audience audience, Identified source)
	{
		audience.sendMessage(source, asComponent());
		return self();
	}
	
	/**
	 * Converts into a component action bar and
	 * sends it to the provided audience.
	 *
	 * @param audience  the audience to receive components
	 * @return  self (for method chaining)
	 *
	 * @see Audience#sendActionBar(Component)
	 */
	default S actionBarToAudience(Audience audience)
	{
		audience.sendActionBar(asComponent());
		return self();
	}
}
