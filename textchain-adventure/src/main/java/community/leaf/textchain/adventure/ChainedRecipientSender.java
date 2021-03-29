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
     * @return  an audience for the recipient
     */
    Audience getRecipientAudience(R recipient);
    
    /**
     * Converts into a component message and sends
     * it to the provided recipient.
     *
     * @param recipient     the recipient to receive components
     * @return  self (for method chaining)
     *
     * @see ChainedAudienceSender#send(Audience)
     */
    default S send(R recipient) { return send(getRecipientAudience(recipient)); }
    
    /**
     * Converts into a component message and sends
     * it to the provided recipient, identified by
     * the provided identity.
     *
     * @param recipient     the recipient to receive components
     * @param source        the identity from whom the
     *                      components originate
     * @return  self (for method chaining)
     *
     * @see ChainedAudienceSender#send(Audience, Identity)
     */
    default S send(R recipient, Identity source) { return send(getRecipientAudience(recipient), source); }
    
    /**
     * Converts into a component message and sends
     * it to the provided recipient, identified by
     * the source's identity.
     *
     * @param recipient     the recipient to receive components
     * @param source        the identifiable source from whom
     *                      the components originate
     * @return  self (for method chaining)
     *
     * @see ChainedAudienceSender#send(Audience, Identified)
     */
    default S send(R recipient, Identified source) { return send(getRecipientAudience(recipient), source); }
    
    /**
     * Converts into a component action bar and
     * sends it to the provided recipient.
     *
     * @param recipient     the recipient to receive components
     * @return  self (for method chaining)
     *
     * @see ChainedAudienceSender#actionBar(Audience)
     */
    default S actionBar(R recipient) { return actionBar(getRecipientAudience(recipient)); }
}