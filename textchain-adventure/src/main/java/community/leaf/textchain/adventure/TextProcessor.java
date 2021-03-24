package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.function.Function;

@FunctionalInterface
public interface TextProcessor extends Function<String, TextComponent>
{
    static TextComponent none(String text)
    {
        return Component.text(text);
    }
    
    static TextComponent legacyAmpersand(String text)
    {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }
}
