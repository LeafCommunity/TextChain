package community.leaf.textchain.examples.bukkit;

import community.leaf.textchain.adventure.TextChain;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

public class TextChainExamplePlugin extends JavaPlugin
{
    BukkitAudiences audiences;
    
    @Override
    public void onEnable()
    {
        this.audiences = BukkitAudiences.create(this);
        audiences.console().sendMessage(TextChain.of("Loaded successfully.").color(NamedTextColor.GOLD));
    }
}
