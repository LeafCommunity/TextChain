/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;

/**
 * Sends components to a recipient by
 * first converting it to an audience.
 *
 * @param <R>   recipient type
 * @param <S>   self-returning subtype
 *              (for method chaining)
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ChainedRecipientSender<R, S extends ChainedRecipientSender<R, S>> extends ChainedAudienceSender<S>
{
    /**
     * Converts the recipient into an audience.
     *
     * @param recipient     the recipient
     * @return an audience for the recipient
     */
    Audience recipientToAudience(R recipient);
    
    /**
     * Converts into a component message and sends
     * it to the provided recipient.
     *
     * @param recipient     the recipient to receive components
     * @return self (for method chaining)
     *
     * @see ChainedAudienceSender#sendToAudience(Audience)
     */
    default S sendToRecipient(R recipient) { return sendToAudience(recipientToAudience(recipient)); }
    
    /**
     * Converts into a component message and sends
     * it to the provided recipient, identified by
     * the provided identity.
     *
     * @param recipient     the recipient to receive components
     * @param source        the identity from whom the
     *                      components originate
     * @return self (for method chaining)
     *
     * @see ChainedAudienceSender#sendToAudience(Audience, Identity)
     */
    default S sendToRecipient(R recipient, Identity source) { return sendToAudience(recipientToAudience(recipient), source); }
    
    /**
     * Converts into a component message and sends
     * it to the provided recipient, identified by
     * the source's identity.
     *
     * @param recipient     the recipient to receive components
     * @param source        the identifiable source from whom
     *                      the components originate
     * @return self (for method chaining)
     *
     * @see ChainedAudienceSender#sendToAudience(Audience, Identified)
     */
    default S sendToRecipient(R recipient, Identified source) { return sendToAudience(recipientToAudience(recipient), source); }
    
    /**
     * Converts into a component action bar and
     * sends it to the provided recipient.
     *
     * @param recipient     the recipient to receive components
     * @return self (for method chaining)
     *
     * @see ChainedAudienceSender#actionBarToAudience(Audience)
     */
    default S actionBarToRecipient(R recipient) { return actionBarToAudience(recipientToAudience(recipient)); }
}
