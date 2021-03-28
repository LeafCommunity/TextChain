package community.leaf.textchain.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

/**
 * Sends components to an audience.
 *
 * @param <S>   self-returning subtype
 *              (for method chaining)
 */
public interface AudienceSender<S extends AudienceSender<S>>
{
    /**
     * Sends component messages to the provided audience.
     *
     * @param audience  the audience to receive components
     * @return  self (for method chaining)
     *
     * @see Audience#sendMessage(Component)
     */
    S send(Audience audience);
    
    /**
     * Sends component messages to the provided audience,
     * identified by the provided identity.
     *
     * @param audience  the audience to receive components
     * @param source    the identity from whom the
     *                  components originate
     * @return  self (for method chaining)
     *
     * @see Audience#sendMessage(Identity, Component)
     */
    S send(Audience audience, Identity source);
    
    /**
     * Sends component messages to the provided audience,
     * identified by the source's identity.
     *
     * @param audience  the audience to receive components
     * @param source    the identifiable source from whom
     *                  the components originate
     * @return  self (for method chaining)
     *
     * @see Audience#sendMessage(Identified, Component)
     */
    S send(Audience audience, Identified source);
    
    /**
     * Sends component action bars to the provided
     * audience.
     *
     * @param audience  the audience to receive components
     * @return  self (for method chaining)
     *
     * @see Audience#sendActionBar(Component)
     */
    S actionBar(Audience audience);
}
