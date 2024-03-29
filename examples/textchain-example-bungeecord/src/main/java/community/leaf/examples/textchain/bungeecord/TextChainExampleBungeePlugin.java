/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.examples.textchain.bungeecord;

import community.leaf.textchain.adventure.TextChain;
import community.leaf.textchain.platforms.bungeecord.BungeeTextChainSource;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import pl.tlinkowski.annotation.basic.NullOr;

public class TextChainExampleBungeePlugin extends Plugin implements BungeeTextChainSource
{
    private @NullOr BungeeAudiences audiences;
    
    @Override
    public void onEnable()
    {
        this.audiences = BungeeAudiences.create(this);
        getProxy().getPluginManager().registerCommand(this, new ExampleCommand(this));
        
        TextChain.using(this).chain()
            .then("Enabled: ")
                .color(NamedTextColor.LIGHT_PURPLE)
            .then("TextChain Example (BungeeCord version)")
            .sendToRecipient(getProxy().getConsole());
    }
    
    @Override
    public void onDisable()
    {
        if (this.audiences != null)
        {
            this.audiences.close();
            this.audiences = null;
        }
    }
    
    @Override
    public BungeeAudiences adventure()
    {
        if (this.audiences != null) { return this.audiences; }
        throw new IllegalStateException("Audiences not initialized (plugin is disabled).");
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
            TextChain.using(plugin).chain()
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
                .sendToRecipient(sender);
        }
    }
}
