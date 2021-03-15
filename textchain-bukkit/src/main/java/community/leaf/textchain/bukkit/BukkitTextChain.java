package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.adventure.TextChainConstructor;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;

public class BukkitTextChain implements TextChain<BukkitTextChain>
{
    private final BukkitAudiences audiences;
    private final WrappedTextComponentBuilder builder;
    
    public BukkitTextChain(BukkitAudiences audiences, WrappedTextComponentBuilder builder)
    {
        this.audiences = audiences;
        this.builder = builder;
    }
    
    @Override
    public WrappedTextComponentBuilder getBuilder() { return builder; }
    
    @Override
    public TextChainConstructor<BukkitTextChain> getConstructor()
    {
        return builder -> new BukkitTextChain(audiences, builder);
    }
    
    public BukkitTextChain send(CommandSender sender)
    {
        return send(audiences.sender(sender));
    }
}
