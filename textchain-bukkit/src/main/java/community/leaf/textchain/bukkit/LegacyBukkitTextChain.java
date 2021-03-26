package community.leaf.textchain.bukkit;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.TextComponent;

public final class LegacyBukkitTextChain extends BukkitChain<LegacyBukkitTextChain>
{
    public LegacyBukkitTextChain(WrappedTextComponentBuilder builder, BukkitAudiences audiences)
    {
        super(builder, audiences);
    }
    
    @Override
    protected ChainConstructor<LegacyBukkitTextChain> getConstructor()
    {
        return builder -> new LegacyBukkitTextChain(builder, getAudiences());
    }
    
    @Override
    protected TextComponent processText(String text)
    {
        return LegacyBukkitComponentSerializer.legacyHexAmpersand().deserialize(text);
    }
}
