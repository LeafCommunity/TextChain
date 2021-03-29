package community.leaf.examples.textchain.bukkit;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.ShowItems;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class VillagerPickpocketListener implements Listener
{
    private final TextChainExampleBukkitPlugin plugin;
    
    public VillagerPickpocketListener(TextChainExampleBukkitPlugin plugin)
    {
        this.plugin = plugin;
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
        
        ShowItems.setItemDisplayName(
            emerald,
            TextChain.chain()
                .then("Villager's Emerald")
                    .italic()
                    .color(TextColor.color(0xadfc85))
        );
        
        ShowItems.setItemLore(
            emerald,
            TextChain.reset()
                .then("Wow, you ")
                .then("pickpocketed")
                    .underlined()
                .then(" that emerald...")
                .next("Thief!")
                    .bold()
                    .italic()
                    .color(TextColor.color(0xfc3b1e))
        );
        
        player.getInventory().addItem(emerald);
        
        TextChain.chain(plugin)
            .then("Pickpocket!")
                .bold().italic().color(TextColor.color(0xfc3b1e))
            .actionBar(player);
    }
}
