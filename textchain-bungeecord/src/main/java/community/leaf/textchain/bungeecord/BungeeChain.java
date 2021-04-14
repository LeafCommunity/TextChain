package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.Chain;
import community.leaf.textchain.adventure.ChainedRecipientSender;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import community.leaf.textchain.platforms.AdventureSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;

import java.util.Objects;

public abstract class BungeeChain<C extends BungeeChain<C>> extends Chain<C>
    implements AdventureSource<BungeeAudiences>, ChainedRecipientSender<CommandSender, C>
{
    private final BungeeAudiences audiences;
    
    public BungeeChain(WrappedTextComponentBuilder builder, BungeeAudiences audiences)
    {
        super(builder);
        this.audiences = Objects.requireNonNull(audiences, "audiences");
    }
    
    @Override
    public final BungeeAudiences adventure() { return audiences; }
    
    @Override
    public final Audience recipientToAudience(CommandSender recipient)
    {
        return adventure().sender(recipient);
    }
}
