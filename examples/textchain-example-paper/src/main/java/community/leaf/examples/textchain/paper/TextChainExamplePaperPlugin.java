package community.leaf.examples.textchain.paper;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.ShowItems;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TextChainExamplePaperPlugin extends JavaPlugin implements Listener
{
    private final Showcase showcase = new Showcase();
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        
        TextChain.of("Enabled: ")
                .color(NamedTextColor.GOLD)
            .then("TextChain Example (Paper version)")
            .send(getServer().getConsoleSender())
            .send(showcase);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        TextChain.of("TextChain Example: ")
            .thenExtra(extra ->
                extra
                    .then("click ")
                    .then("here")
                        .underline()
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
            .then(ShowItems.asText(placed))
                .color(NamedTextColor.RED)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(placed)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_RED)
            .then(" using ")
            .then(ShowItems.asText(hand))
                .color(NamedTextColor.AQUA)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.asClientName(hand)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_AQUA)
            .send(player)
            .send(showcase);
    
        ShowItems.Rarity rarity = ShowItems.rarity(hand);
        
        TextChain.of("Rarity of ")
            .then(ShowItems.asText(hand))
                .color(rarity.getColor())
            .then(" is ")
            .then(rarity.name())
                .color(rarity.getColor())
                .bold()
                .italic()
            .send(player)
            .send(showcase);
    }
    
    private class Showcase implements Audience
    {
        @Override
        public void sendMessage(final @NonNull Identity source, final @NonNull Component message, final @NonNull MessageType type)
        {
            TextChain.of("Sent JSON component: ")
                .then(GsonComponentSerializer.gson().serialize(message))
                .color(NamedTextColor.YELLOW)
                .send(getServer().getConsoleSender());
        }
    }
}
