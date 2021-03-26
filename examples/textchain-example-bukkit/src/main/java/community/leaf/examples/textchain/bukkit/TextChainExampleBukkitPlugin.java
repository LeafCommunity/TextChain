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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
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
    
        TextChain.of("Enabled: ")
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
        TextChain.using(this)
            .then("Hello ")
                .color(NamedTextColor.GOLD)
                .bold()
                .extra(inherits -> inherits.then("friend!").italic())
            .then(" ")
            .then("How are you?")
                .color(TextColor.color(0xFF0000))
                .tooltip("Click here to respond with \"pretty good\"")
                .suggest("pretty good")
            .send(sender)
            .send(showcase);
            
        return true;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        ItemStack broken = new ItemStack(event.getBlock().getType());
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        TextChain.using(this)
            .then("You broke ")
            .then(ShowItems.asComponentInBrackets(broken))
                .color(NamedTextColor.RED)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(broken)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_RED)
            .then(" using ")
            .then(ShowItems.asComponentInBrackets(tool))
                .color(NamedTextColor.AQUA)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(tool)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_AQUA)
            .send(player)
            .send(showcase);
        
        ItemRarity rarity = ShowItems.rarity(tool);
        
        TextChain.of("Rarity of ")
            .then(ShowItems.asComponentInBrackets(tool))
                .color(rarity)
            .then(" is ")
            .then(rarity)
                .bold()
                .italic()
            .send(getAudiences().player(player))
            .send(showcase);
    }
    
    @EventHandler
    public void onClickOnEntity(PlayerInteractEntityEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND) { return; }
        
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();
        
        TextChain.of("You clicked on ")
            .then(ShowEntities.asComponent(clicked))
                .color(TextColor.color(0xe0c0e8))
            .then(".")
            .send(getAudiences().player(player))
            .send(showcase);
    }
    
    @EventHandler
    public void onSneakNearVillager(PlayerToggleSneakEvent event)
    {
        if (!event.isSneaking()) { return; }
        
        Player player = event.getPlayer();
        
        boolean isNearVillager =
            player.getNearbyEntities(4, 2, 4).stream()
                .map(Entity::getType)
                .anyMatch(type -> type == EntityType.VILLAGER);
        
        if (!isNearVillager) { return; }
        
        ItemStack emerald = new ItemStack(Material.EMERALD);
        
        ShowItems.setDisplayName(emerald,
            TextChain.of("Villager's Emerald").italic().color(TextColor.color(0xadfc85))
        );
        
        ShowItems.setLore(emerald,
            TextChain.reset()
                .then("Wow! You ")
                .then("pickpocketed")
                    .underlined()
                .then(" that emerald...")
                .next("Thief!")
                    .bold()
                    .italic()
                    .color(TextColor.color(0xfc3b1e))
        );
        
        player.getInventory().addItem(emerald);
        
        TextChain.using(this)
            .then("Pickpocket!")
                .bold().italic().color(TextColor.color(0xfc3b1e))
            .actionBar(player);
    }
    
    private class Showcase implements Audience
    {
        @Override
        public void sendMessage(final Identity source, final Component message, final MessageType type)
        {
            TextChain.of("Sent JSON component: ")
                .then(GsonComponentSerializer.gson().serialize(message))
                .color(NamedTextColor.AQUA)
                .send(audiences.console());
        }
    }
}
