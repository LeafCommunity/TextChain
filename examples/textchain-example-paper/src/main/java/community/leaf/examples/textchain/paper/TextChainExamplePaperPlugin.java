package community.leaf.examples.textchain.paper;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import community.leaf.textchain.adventure.ItemRarity;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.ShowEntities;
import community.leaf.textchain.bukkit.ShowItems;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TextChainExamplePaperPlugin extends JavaPlugin implements Listener
{
    private final Showcase showcase = new Showcase();
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        
        TextChain.empty()
            .then("Enabled: ")
                .color(NamedTextColor.GOLD)
            .then("TextChain Example (Paper version)")
            .send((Audience) getServer().getConsoleSender())
            .send(showcase);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        TextChain.empty()
            .then("TextChain Example: ")
            .then()
                .extra(group -> group
                    .then("click ")
                    .then("here")
                        .underlined()
                        .color(TextColor.color(0x0000FF))
                        .link("https://google.com")
                        .tooltip("Click to visit: &9&ogoogle.com")
                    .then(" to visit a ")
                    .then("website")
                        .italic()
                        .bold()
                )
                .color(TextColor.color(0xD8EFFF))
            .then(" ... ")
                .color(TextColor.color(0x444444))
            .then("or maybe you're looking for a &osuggestion?")
                .tooltip("Click for a suggestion. . .")
                .suggest("hello ")
            .send(sender)
            .send(showcase);
        
        return true;
    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        ItemStack placed = new ItemStack(event.getBlockPlaced().getType());
        ItemStack hand = player.getInventory().getItemInMainHand();
        
        TextChain.of("You placed ")
            .then(ShowItems.asComponentInBrackets(placed))
                .color(NamedTextColor.RED)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(placed)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_RED)
            .then(" using ")
            .then(ShowItems.asComponentInBrackets(hand))
                .color(NamedTextColor.AQUA)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(hand)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_AQUA)
            .send((Audience) player)
            .send(showcase);
    
        ItemRarity rarity = ShowItems.rarity(hand);
        
        TextChain.of("Rarity of ")
            .then(ShowItems.asComponentInBrackets(hand))
                .color(rarity)
            .then(" is ")
            .then(rarity)
                .bold()
                .italic()
            .send((Audience) player)
            .send(showcase);
    }
    
    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player)) { return; }
        
        Player player = (Player) event.getDamager();
        Entity damaged = event.getEntity();
        
        TextChain.of("You damaged ")
            .then(ShowEntities.asComponent(damaged))
                .color(TextColor.color(0xe8c3ae))
            .then("!")
            .send((Audience) player)
            .send(showcase);
    }
    
    @EventHandler
    public void onJumpNearBunnies(PlayerJumpEvent event)
    {
        Player player = event.getPlayer();
        
        boolean isBunny =
            player.getNearbyEntities(10, 10, 10).stream()
                .map(Entity::getType)
                .anyMatch(type -> type == EntityType.RABBIT);
        
        if (!isBunny) { return; }
        
        ItemStack gift = new ItemStack(Material.GOLDEN_CARROT);
        ItemMeta meta = gift.getItemMeta();
        
        meta.displayName(
            TextChain.reset()
                .extra(chain -> chain
                    .then("A ")
                    .then("very ")
                        .bold()
                    .then("lovely, sparkly ")
                        .italic()
                    .then("gift")
                        .bold()
                        .color(TextColor.color(0xfcad25))
                    .then("!")
                )
                .color(TextColor.color(0xfcee25))
                .asComponent()
        );
        
        List<Component> lores = new ArrayList<>();
        
        lores.add(
            TextChain.reset()
                .then("You're a ")
                .then("bunny")
                    .bold()
                .then("?!")
                .asComponent()
        );
        
        lores.add(TextChain.of("Eat up.").asComponent());
        
        meta.lore(lores);
        gift.setItemMeta(meta);
        player.getInventory().addItem(gift);
        
        TextChain.of("Hop!")
                .bold()
                .italic()
                .color(TextColor.color(0x443344))
            .then(" Have a ")
            .then("treat")
                .italic()
                .color(TextColor.color(0xfcad25))
                .hover(ShowItems.asHover(gift))
            .then(", you silly rabbit.")
            .send((Audience) player)
            .send(showcase);
    }
    
    private class Showcase implements Audience
    {
        @Override
        public void sendMessage(final Identity source, final Component message, final MessageType type)
        {
            TextChain.of("Sent JSON component: ")
                .then(GsonComponentSerializer.gson().serialize(message))
                .color(NamedTextColor.YELLOW)
                .send((Audience) getServer().getConsoleSender());
        }
    }
}
