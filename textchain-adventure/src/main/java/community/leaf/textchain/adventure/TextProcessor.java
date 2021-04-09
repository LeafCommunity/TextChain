package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.function.Function;

/**
 * Converts a string into a text component.
 */
@FunctionalInterface
public interface TextProcessor extends Function<String, TextComponent>
{
    /**
     * Simply creates a text component containing
     * the input string. The text isn't "processed"
     * beyond that.
     *
     * @param text  a string
     * @return  a new component containing the input text
     */
    static TextComponent none(String text)
    {
        return Component.text(text);
    }
    
    /**
     * Creates a text component by parsing legacy
     * ampersand-style color codes contained
     * within the input string.
     *
     * @param text  text containing legacy ampersand-style
     *              color codes
     * @return  a new component as a result of
     *          processing ampersand color codes
     */
    static TextComponent legacyAmpersand(String text)
    {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }
}