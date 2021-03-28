package community.leaf.examples.textchain.bukkit;

import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.BukkitTextChainSource;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("NotNullFieldNotInitialized")
public class TextChainExampleBukkitPlugin extends JavaPlugin implements BukkitTextChainSource, Listener
{
    private BukkitAudiences audiences;
    private Showcase showcase;
    
    @Override
    public void onEnable()
    {
        this.audiences = BukkitAudiences.create(this);
        this.showcase = new Showcase();
        
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new VillagerPickpocketListener(this), this);
        
        TextChain.chain()
            .then("Enabled: ")
                .color(NamedTextColor.DARK_AQUA)
            .then("TextChain Example (Bukkit version)")
            .send(audiences.console())
            .send(showcase);
    }
    
    @Override
    public BukkitAudiences getAudiences() { return audiences; }
    
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
            .send(getAudiences().sender(sender))
            .send(showcase);
            
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
            .then(ShowItems.itemComponentInBrackets(broken))
                .color(NamedTextColor.RED)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.itemClientName(broken)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_RED)
            .then(" using ")
            .then(ShowItems.itemComponentInBrackets(tool))
                .color(NamedTextColor.AQUA)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.itemClientName(tool)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_AQUA)
            .send(player)
            .send(showcase);
        
        ItemRarity rarity = ShowItems.rarity(tool);
        
        TextChain.chain(this)
            .then("Rarity of ")
            .then(ShowItems.itemComponentInBrackets(tool))
                .color(rarity)
            .then(" is ")
            .then(rarity)
                .bold()
                .italic()
            .send(player)
            .send(showcase);
    }
    
    @EventHandler
    public void onClickOnEntity(PlayerInteractEntityEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND) { return; }
        
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();
        
        TextChain.chain(this)
            .then("You clicked on ")
            .then(ShowEntities.entityComponent(clicked))
                .color(TextColor.color(0xe0c0e8))
            .then(".")
            .send(player)
            .send(showcase);
    }
    
    private class Showcase implements Audience
    {
        @Override
        public void sendMessage(final Identity source, final Component message, final MessageType type)
        {
            TextChain.chain()
                .then("Sent JSON component: ")
                .then(GsonComponentSerializer.gson().serialize(message))
                .color(NamedTextColor.AQUA)
                .send(audiences.console());
        }
    }
}
