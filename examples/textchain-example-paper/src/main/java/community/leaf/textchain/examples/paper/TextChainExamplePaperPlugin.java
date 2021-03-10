package community.leaf.textchain.examples.paper;

import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TextChainExamplePaperPlugin extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        TextChain.of("&aEnabled:&f TextChain Example (Paper version)").send(getServer().getConsoleSender());
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        TextChain.of("TextChain Example: ")
            .thenExtra(extra -> extra
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
            .send(sender);
        
        return true;
    }
}
