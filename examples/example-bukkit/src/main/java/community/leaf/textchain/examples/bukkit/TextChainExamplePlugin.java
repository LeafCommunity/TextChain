package community.leaf.textchain.examples.bukkit;

import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TextChainExamplePlugin extends JavaPlugin
{
    private BukkitAudiences audiences;
    
    @Override
    public void onEnable()
    {
        this.audiences = BukkitAudiences.create(this);
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
            .asComponent();
        
        audiences.sender(sender).sendMessage(component);
        sender.sendMessage("As JSON: " + GsonComponentSerializer.gson().serialize(component));
        
        return true;
    }
}
