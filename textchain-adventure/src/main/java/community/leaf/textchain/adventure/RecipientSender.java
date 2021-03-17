package community.leaf.textchain.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;

public interface RecipientSender<R, S extends RecipientSender<R, S>> extends AudienceSender<S>
{
    Audience getRecipientAudience(R recipient);
    
    default S send(R recipient) { return send(getRecipientAudience(recipient)); }
    
    default S send(R recipient, Identity source) { return send(getRecipientAudience(recipient), source); }
    
    default S send(R recipient, Identified source) { return send(getRecipientAudience(recipient), source); }
    
    default S actionBar(R recipient) { return actionBar(getRecipientAudience(recipient)); }
}
