package community.leaf.textchain.adventure;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public enum LegacyColorCodeAliases
{
    BLACK('0', NamedTextColor.BLACK),
    DARK_BLUE('1', NamedTextColor.DARK_BLUE),
    DARK_GREEN('2', NamedTextColor.DARK_GREEN),
    DARK_AQUA('3', NamedTextColor.DARK_AQUA, "dark_cyan"),
    DARK_RED('4', NamedTextColor.DARK_RED),
    DARK_PURPLE('5', NamedTextColor.DARK_PURPLE, "purple"),
    GOLD('6', NamedTextColor.GOLD, "orange"),
    GRAY('7', NamedTextColor.GRAY, "light_gray", "light_grey", "grey"),
    DARK_GRAY('8', NamedTextColor.DARK_GRAY, "dark_grey"),
    BLUE('9', NamedTextColor.BLUE, "violet"),
    GREEN('a', NamedTextColor.GREEN, "light_green", "lime_green", "lime"),
    AQUA('b', NamedTextColor.AQUA, "light_aqua", "light_cyan", "cyan"),
    RED('c', NamedTextColor.RED, "light_red"),
    LIGHT_PURPLE('d', NamedTextColor.LIGHT_PURPLE, "pink"),
    YELLOW('e', NamedTextColor.YELLOW),
    WHITE('f', NamedTextColor.WHITE),
    OBFUSCATED('k', TextDecoration.OBFUSCATED, "magic"),
    BOLD('l', TextDecoration.BOLD),
    STRIKETHROUGH('m', TextDecoration.STRIKETHROUGH),
    UNDERLINED('n', TextDecoration.UNDERLINED, "underline"),
    ITALIC('o', TextDecoration.ITALIC),
    RESET('r', Reset.INSTANCE, "clear");
    
    private static final Map<Character, LegacyColorCodeAliases> aliasesByCode = new HashMap<>();
    private static final Map<TextFormat, LegacyColorCodeAliases> aliasesByFormat = new HashMap<>();
    private static final Map<String, LegacyColorCodeAliases> aliasesByStrict = new HashMap<>();
    private static final Map<String, LegacyColorCodeAliases> aliasesByExtended = new HashMap<>();
    
    static
    {
        for (LegacyColorCodeAliases alias : values())
        {
            aliasesByCode.put(alias.character, alias);
            aliasesByFormat.put(alias.format, alias);
            alias.strictAliases.forEach(mutation -> aliasesByStrict.put(mutation, alias));
            alias.extendedAliases.forEach(mutation -> aliasesByExtended.put(mutation, alias));
        }
    }
    
    private final Set<String> strictAliases = new LinkedHashSet<>(getMutationsOfAlias(name()));
    private final Set<String> extendedAliases = new LinkedHashSet<>();
    
    private final char character;
    private final TextFormat format;
    
    LegacyColorCodeAliases(char character, TextFormat format, String ... aliases)
    {
        this.character = character;
        this.format = format;
        this.extendedAliases.addAll(strictAliases);
        
        Arrays.stream(aliases).map(this::getMutationsOfAlias).forEach(this.extendedAliases::addAll);
    }
    
    private List<String> getMutationsOfAlias(String alias)
    {
        List<String> mutations = new ArrayList<>();
        String lowercase = alias.toLowerCase();
        
        mutations.add(lowercase);
        mutations.add(lowercase.replace("_", "-"));
        mutations.add(lowercase.replace("_", ""));
        
        return mutations;
    }
    
    public char getCharacter() { return character; }
    
    public TextFormat getFormat() { return format; }
    
    public Set<String> getStrictAliases() { return Collections.unmodifiableSet(strictAliases); }
    
    public Set<String> getAllAliases() { return Collections.unmodifiableSet(extendedAliases); }
    
    public boolean isColor() { return format instanceof NamedTextColor; }
    
    public Optional<NamedTextColor> asColor()
    {
        return (isColor()) ? Optional.of((NamedTextColor) format) : Optional.empty();
    }
    
    public boolean isDecoration() { return format instanceof TextDecoration; }
    
    public Optional<TextDecoration> asDecoration()
    {
        return (isDecoration()) ? Optional.of((TextDecoration) format) : Optional.empty();
    }
    
    public boolean isReset() { return format == Reset.INSTANCE; }
    
    public static Optional<LegacyColorCodeAliases> resolveByCharacter(char code)
    {
        return Optional.ofNullable(aliasesByCode.get(Character.toLowerCase(code)));
    }
    
    public static Optional<LegacyColorCodeAliases> resolveByFormat(TextFormat format)
    {
        return Optional.ofNullable(aliasesByFormat.get(format));
    }
    
    public static LegacyColorCodeAliases of(TextFormat format)
    {
        return resolveByFormat(format).orElseThrow(() ->
            new IllegalArgumentException("Unsupported format: " + format)
        );
    }
    
    private static Optional<LegacyColorCodeAliases> resolveByAlias(Map<String, LegacyColorCodeAliases> map, String alias)
    {
        return Optional.ofNullable(map.get(alias.toLowerCase()));
    }
    
    public static Optional<LegacyColorCodeAliases> resolveByAlias(String alias)
    {
        return resolveByAlias(aliasesByExtended, alias);
    }
    
    public static Optional<LegacyColorCodeAliases> resolveByStrictAlias(String alias)
    {
        return resolveByAlias(aliasesByStrict, alias);
    }
    
    public static final class Reset implements TextFormat
    {
        public static final Reset INSTANCE = new Reset();
        
        private Reset() {}
    }
}
