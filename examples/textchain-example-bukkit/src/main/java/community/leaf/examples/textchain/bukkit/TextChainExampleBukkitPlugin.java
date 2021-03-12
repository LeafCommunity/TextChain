package community.leaf.examples.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.ShowItems;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TextChainExampleBukkitPlugin extends JavaPlugin implements Listener
{
    private BukkitAudiences audiences;
    
    @Override
    public void onEnable()
    {
        this.audiences = BukkitAudiences.create(this);
        getServer().getPluginManager().registerEvents(this, this);
        
        audiences.console().sendMessage(TextChain.of("Loaded successfully.").color(NamedTextColor.GOLD));
    }
    
    public BukkitAudiences getAudiences() { return audiences; }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        TextComponent component = TextChain.empty()
            .then("Hello ")
                .color(NamedTextColor.GOLD)
                .bold()
                .extra(inherits -> inherits.then("friend!").italic())
            .then(" ")
            .then("How are you?")
                .color(TextColor.color(0xFF0000))
                .tooltip("Click here to respond with \"pretty good\"")
                .suggest("pretty good")
            .send(audiences.sender(sender))
            .asComponent();
        
        TextChain.of("Sent JSON component: ")
            .then(GsonComponentSerializer.gson().serialize(component))
                .color(NamedTextColor.AQUA)
            .send(audiences.console());
        return true;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ItemStack broken = new ItemStack(event.getBlock().getType());
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        TextComponent component =
            TextChain.of("You broke ")
                .then(ShowItems.asText(broken))
                    .color(NamedTextColor.RED)
                .then(" using ")
                .then(ShowItems.asText(tool))
                    .color(NamedTextColor.AQUA)
                .send(audiences.player(player))
                .asComponent();
        
        TextChain.of("Sent JSON component: ")
            .then(GsonComponentSerializer.gson().serialize(component))
                .color(NamedTextColor.AQUA)
            .send(audiences.console());
    }
}
