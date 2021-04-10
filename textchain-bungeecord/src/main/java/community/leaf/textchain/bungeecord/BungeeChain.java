package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainedRecipientSender;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;

import java.util.Objects;

public abstract class BungeeChain<C extends BungeeChain<C>> extends Chain<C> implements ChainedRecipientSender<CommandSender, C>
{
    private final BungeeAudiences audiences;
    
    public BungeeChain(WrappedTextComponentBuilder builder, BungeeAudiences audiences)
    {
        super(builder);
        this.audiences = Objects.requireNonNull(audiences, "audiences");
    }
    
    public final BungeeAudiences getAudiences() { return audiences; }
    
    @Override
    public Audience getRecipientAudience(CommandSender recipient)
    {
        return audiences.sender(recipient);
    }
}
