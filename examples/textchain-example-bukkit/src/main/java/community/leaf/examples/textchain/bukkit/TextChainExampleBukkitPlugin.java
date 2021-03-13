package community.leaf.examples.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.ShowEntities;
import community.leaf.textchain.bukkit.ShowItems;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TextChainExampleBukkitPlugin extends JavaPlugin implements Listener
{
    private BukkitAudiences audiences;
    private Showcase showcase;
    
    @Override
    public void onEnable()
    {
        this.audiences = BukkitAudiences.create(this);
        this.showcase = new Showcase();
        
        getServer().getPluginManager().registerEvents(this, this);
    
        TextChain.of("Enabled: ")
                .color(NamedTextColor.DARK_AQUA)
            .then("TextChain Example (Bukkit version)")
            .send(audiences.console())
            .send(showcase);
    }
    
    public BukkitAudiences getAudiences() { return audiences; }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        TextChain.empty()
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
            .send(showcase);
            
        return true;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ItemStack broken = new ItemStack(event.getBlock().getType());
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        TextChain.of("You broke ")
            .then(ShowItems.asText(broken))
                .color(NamedTextColor.RED)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(broken)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_RED)
            .then(" using ")
            .then(ShowItems.asText(tool))
                .color(NamedTextColor.AQUA)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(tool)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_AQUA)
            .send(audiences.player(player))
            .send(showcase);
    
        ShowItems.Rarity rarity = ShowItems.rarity(tool);
        
        TextChain.of("Rarity of ")
            .then(ShowItems.asText(tool))
                .color(rarity.getColor())
            .then(" is ")
            .then(rarity.name())
                .color(rarity.getColor())
                .bold()
                .italic()
            .send(audiences.player(player))
            .send(showcase);
    }
    
    @EventHandler
    public void onClickOnEntity(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();
        
        TextChain.of("You clicked on ")
            .then(ShowEntities.asName(clicked))
                .hover(ShowEntities.asHover(clicked))
                .color(TextColor.color(0xd5d8e5))
            .send(audiences.player(player))
            .send(showcase);
    }
    
    private class Showcase implements Audience
    {
        @Override
        public void sendMessage(final @NonNull Identity source, final @NonNull Component message, final @NonNull MessageType type)
        {
            TextChain.of("Sent JSON component: ")
                .then(GsonComponentSerializer.gson().serialize(message))
                .color(NamedTextColor.AQUA)
                .send(audiences.console());
        }
    }
}
