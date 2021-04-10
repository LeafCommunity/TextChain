package community.leaf.examples.textchain.bungeecord;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bungeecord.BungeeTextChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("NotNullFieldNotInitialized")
public class TextChainExampleBungeePlugin extends Plugin implements BungeeTextChainSource
{
    private BungeeAudiences audiences;
    
    @Override
    public void onEnable()
    {
        this.audiences = BungeeAudiences.create(this);
        getProxy().getPluginManager().registerCommand(this, new ExampleCommand(this));
        
        TextChain.chain(this)
            .then("Enabled: ")
                .color(NamedTextColor.LIGHT_PURPLE)
            .then("TextChain Example (BungeeCord version)")
            .send(getProxy().getConsole());
    }
    
    @Override
    public BungeeAudiences getAudiences()
    {
        return audiences;
    }
    
    public static class ExampleCommand extends Command
    {
        TextChainExampleBungeePlugin plugin;
        
        public ExampleCommand(TextChainExampleBungeePlugin plugin)
        {
            super("textchainbungee");
            this.plugin = plugin;
        }
    
        @Override
        public void execute(CommandSender sender, String[] args)
        {
            TextChain.chain(plugin)
                .then("Why, yes! ")
                    .italic()
                .then("This is a text chain...")
                    .color(NamedTextColor.DARK_PURPLE)
                    .bold()
                    .tooltip(tip -> tip
                        .then("Incredible!")
                            .color(NamedTextColor.RED)
                    )
                .then(" on BungeeCord")
                    .color(NamedTextColor.LIGHT_PURPLE)
                    .tooltip("Click to check the global list.")
                    .command("/glist")
                .send(sender);
        }
    }
}
