package community.leaf.examples.textchain.paper;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.bukkit.BukkitToAdventure;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BunnyJumpListener implements Listener
{
    private final TextChainExamplePaperPlugin plugin;
    
    public BunnyJumpListener(TextChainExamplePaperPlugin plugin) { this.plugin = plugin; }
    
    @EventHandler
    public void onJumpNearBunnies(PlayerJumpEvent event)
    {
        Player player = event.getPlayer();
        
        boolean isBunny =
            player.getNearbyEntities(5, 3, 5).stream()
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
        
        meta.lore(
            TextChain.reset()
                .then("You're a ")
                .then("bunny")
                    .bold()
                .then("?!")
                .next("Eat up.")
                .asComponentListSplitByNewLine()
        );
        
        gift.setItemMeta(meta);
        player.getInventory().addItem(gift);
        
        TextChain.chain()
            .then("Hop!")
                .bold()
                .italic()
                .color(TextColor.color(0x443344))
            .then(" Have a ")
            .then("treat")
                .italic()
                .color(TextColor.color(0xfcad25))
                .hover(BukkitToAdventure.items().hover(gift))
            .then(", you silly rabbit.")
            .send((Audience) player)
            .send(plugin.exampleAudience());
    }
}
