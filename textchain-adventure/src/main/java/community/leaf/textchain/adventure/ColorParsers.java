package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.function.Function;

public enum ColorParsers
{
    NONE(Component::text),
    SECTION(LegacyComponentSerializer.legacySection()::deserialize),
    AMPERSAND(LegacyComponentSerializer.legacyAmpersand()::deserialize);
    
    private final Function<String, TextComponent> parser;
    
    ColorParsers(Function<String, TextComponent> parser) { this.parser = parser; }
    
    public TextComponent parse(String text) { return parser.apply(text); }
}
