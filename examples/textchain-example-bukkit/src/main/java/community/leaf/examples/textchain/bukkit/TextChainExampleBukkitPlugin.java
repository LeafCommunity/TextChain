package community.leaf.examples.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.platforms.ItemRarity;
import community.leaf.textchain.platforms.bukkit.BukkitTextChainSource;
import community.leaf.textchain.platforms.bukkit.adapters.BukkitToAdventure;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pl.tlinkowski.annotation.basic.NullOr;

public class TextChainExampleBukkitPlugin extends JavaPlugin implements BukkitTextChainSource, Listener
{
    private @NullOr BukkitAudiences audiences;
    
    @Override
    public void onEnable()
    {
        this.audiences = BukkitAudiences.create(this);
        
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new VillagerPickpocketListener(this), this);
        
        TextChain.chain()
            .then("Enabled: ")
                .color(NamedTextColor.DARK_AQUA)
            .then("TextChain Example (Bukkit version)")
            .sendToAudience(audiences.console())
            .sendToAudience(exampleAudience());
    }
    
    @Override
    public void onDisable()
    {
        if (this.audiences != null)
        {
            this.audiences.close();
            this.audiences = null;
        }
    }
    
    @Override
    public BukkitAudiences adventure()
    {
        if (this.audiences != null) { return this.audiences; }
        throw new IllegalStateException("Audiences not initialized (plugin is disabled).");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        TextChain.chain()
            .then("Hello ")
                .color(NamedTextColor.GOLD)
                .bold()
                .extra(inherits -> inherits.then("friend!").italic())
            .then(" ")
            .then("How are you?")
                .color(TextColor.color(0xFF0000))
                .tooltip("Click here to respond with \"pretty good\"")
                .suggest("pretty good")
            .sendToAudience(adventure().sender(sender))
            .sendToAudience(exampleAudience());
            
        return true;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ItemStack broken = new ItemStack(event.getBlock().getType());
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        TextChain.chain(this)
            .then("You broke ")
            .then(BukkitToAdventure.items().componentInBrackets(broken))
                .color(NamedTextColor.RED)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(BukkitToAdventure.items().clientName(broken)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_RED)
            .then(" using ")
            .then(BukkitToAdventure.items().componentInBrackets(tool))
                .color(NamedTextColor.AQUA)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(BukkitToAdventure.items().clientName(tool)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_AQUA)
            .sendToRecipient(player)
            .sendToAudience(exampleAudience());
        
        ItemRarity rarity = BukkitToAdventure.items().rarity(tool);
        
        TextChain.chain(this)
            .then("Rarity of ")
            .then(BukkitToAdventure.items().componentInBrackets(tool))
                .color(rarity)
            .then(" is ")
            .then(rarity)
                .bold()
                .italic()
            .sendToRecipient(player)
            .sendToAudience(exampleAudience());
    }
    
    @EventHandler
    public void onClickOnEntity(PlayerInteractEntityEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND) { return; }
        
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();
        
        TextChain.chain(this)
            .then("You clicked on ")
            .then(BukkitToAdventure.entities().component(clicked))
                .color(TextColor.color(0xe0c0e8))
            .then(".")
            .sendToRecipient(player)
            .sendToAudience(exampleAudience());
    }
    
    //
    //
    //
    
    private static @NullOr IgnoreMe EXAMPLE;
    
    private IgnoreMe exampleAudience()
    {
        if (EXAMPLE == null) { EXAMPLE = new IgnoreMe(); }
        return EXAMPLE;
    }
    
    private class IgnoreMe implements Audience
    {
        @Override
        public void sendMessage(final Identity source, final Component message, final MessageType type)
        {
            TextChain.chain()
                .then("Sent JSON component: ")
                .then(GsonComponentSerializer.gson().serialize(message))
                .color(NamedTextColor.AQUA)
                .sendToAudience(adventure().console());
        }
    }
}
