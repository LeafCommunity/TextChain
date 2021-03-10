package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class BukkitTextChains
{
    private BukkitTextChains() { throw new UnsupportedOperationException(); }
    
    public static Consumer<TextChain> itemHover(ItemStack item)
    {
        return chain -> {}; // TODO
    }
}
