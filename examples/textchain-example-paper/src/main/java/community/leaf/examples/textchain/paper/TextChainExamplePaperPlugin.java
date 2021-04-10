package community.leaf.examples.textchain.paper;

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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pl.tlinkowski.annotation.basic.NullOr;

public class TextChainExamplePaperPlugin extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new BunnyJumpListener(this), this);
        
        TextChain.chain()
            .then("Enabled: ")
                .color(NamedTextColor.GOLD)
            .then("TextChain Example (Paper version)")
            .send((Audience) getServer().getConsoleSender())
            .send(exampleAudience());
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length <= 0)
        {
            TextChain.chain()
                .then("Demo: ")
                .then("[Website]")
                    .color(TextColor.color(0x1533f2))
                    .command("/" + label + " website")
                    .tooltip(tip -> tip
                        .then("Click to run: ")
                        .then("/" + label + " website")
                            .color(TextColor.color(0xc7cbe2))
                    )
                .then(" ")
                .then("[Suggestion]")
                    .color(TextColor.color(0xef5502))
                    .command("/" + label + " suggestion")
                    .tooltip(tip -> tip
                        .then("Click to run: ")
                        .then("/" + label + " suggestion")
                            .color(TextColor.color(0xedd6c9))
                    )
                .then(" ")
                .then("[Insertion]")
                    .color(TextColor.color(0x33ce9a))
                    .command("/" + label + " insertion")
                    .tooltip(tip -> tip
                        .then("Click to run: ")
                        .then("/" + label + " insertion")
                            .color(TextColor.color(0xd0e2dc))
                    )
                .send(sender)
                .send(exampleAudience());
        }
        else if ("website".equalsIgnoreCase(args[0]))
        {
            TextChain.chain()
                .then("Website: ")
                .thenExtra(group -> group
                    .then("Try me!")
                        .italic()
                        .color(TextColor.color(0x1533f2))
                    .then(" (click)")
                        .color(TextColor.color(0xc7cbe2))
                )
                .link("https://github.com/LeafCommunity/TextChain/")
                .tooltip("Click to try: website")
                .send(sender)
                .send(exampleAudience());
        }
        else if ("suggestion".equalsIgnoreCase(args[0]))
        {
            TextChain.chain()
                .then("Suggestion: ")
                .thenExtra(group -> group
                    .then("Try me!")
                        .italic()
                        .color(TextColor.color(0xef5502))
                    .then(" (click)")
                        .color(TextColor.color(0xedd6c9))
                )
                .suggest("/" + label + " insertion <- try this one, eh?")
                .tooltip("Click to try: suggestion")
                .send(sender)
                .send(exampleAudience());
        }
        else if ("insertion".equalsIgnoreCase(args[0]))
        {
            TextChain.chain()
                .then("Insertion: ")
                .thenExtra(group -> group
                    .then("Try me!")
                        .italic()
                        .color(TextColor.color(0x33ce9a))
                    .then(" (shift + click)")
                        .color(TextColor.color(0xd0e2dc))
                )
                .insertion("consider yourself inserted")
                .tooltip("Shift + click to try: insertion")
                .send(sender)
                .send(exampleAudience());
        }
        else
        {
            TextChain.legacy()
                .then("&c&o&lUhoh!&r I'm not sure what ")
                .then("&n" + args[0])
                    .tooltip("&4&oOops??")
                .then(" is.")
                .send(sender)
                .send(exampleAudience());
        }
        
        return true;
    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        ItemStack placed = new ItemStack(event.getBlockPlaced().getType());
        ItemStack hand = player.getInventory().getItemInMainHand();
        
        TextChain.chain()
            .then("You placed ")
            .then(ShowItems.itemComponentInBrackets(placed))
                .color(NamedTextColor.RED)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.itemClientName(placed)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_RED)
            .then(" using ")
            .then(ShowItems.itemComponentInBrackets(hand))
                .color(NamedTextColor.AQUA)
            .then(" ")
            .thenExtra(extra -> extra.then("(").then(ShowItems.itemClientName(hand)).then(")"))
                .italic()
                .color(NamedTextColor.DARK_AQUA)
            .send((Audience) player)
            .send(exampleAudience());
    
        ItemRarity rarity = ShowItems.rarity(hand);
        
        TextChain.chain()
            .then("Rarity of ")
            .then(ShowItems.itemComponentInBrackets(hand))
                .color(rarity)
            .then(" is ")
            .then(rarity)
                .bold()
                .italic()
            .send((Audience) player)
            .send(exampleAudience());
    }
    
    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player)) { return; }
        
        Player player = (Player) event.getDamager();
        Entity damaged = event.getEntity();
        
        TextChain.chain()
            .then("You damaged ")
            .then(ShowEntities.entityComponent(damaged))
                .color(TextColor.color(0xe8c3ae))
            .then("!")
            .send((Audience) player)
            .send(exampleAudience());
    }
    
    //
    //
    //
    
    private static @NullOr Showcase EXAMPLE;
    
    protected Showcase exampleAudience()
    {
        if (EXAMPLE == null) { EXAMPLE = new Showcase(); }
        return EXAMPLE;
    }
    
    private class Showcase implements Audience
    {
        @Override
        public void sendMessage(final Identity source, final Component message, final MessageType type)
        {
            TextChain.chain()
                .then("Sent JSON component: ")
                .then(GsonComponentSerializer.gson().serialize(message))
                .color(NamedTextColor.YELLOW)
                .send((Audience) getServer().getConsoleSender());
        }
    }
}
