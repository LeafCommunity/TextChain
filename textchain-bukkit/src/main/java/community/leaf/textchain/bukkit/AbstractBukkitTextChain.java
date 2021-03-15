package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.AbstractTextChain;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public abstract class AbstractBukkitTextChain<C extends AbstractBukkitTextChain<C>> extends AbstractTextChain<C>
{
    private final BukkitAudiences audiences;
    
    public AbstractBukkitTextChain(WrappedTextComponentBuilder builder, BukkitAudiences audiences)
    {
        super(builder);
        this.audiences = Objects.requireNonNull(audiences, "audiences");
    }
    
    public final BukkitAudiences getAudiences() { return audiences; }
    
    public C send(CommandSender sender) { return send(getAudiences().sender(sender)); }
}
