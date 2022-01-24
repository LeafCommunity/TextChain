/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LegacyColorCodeAlias
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
    RESET('r', Reset.RESET, "clear");
    
    private static final Map<Character, LegacyColorCodeAlias> aliasesByCode = new HashMap<>();
    private static final Map<TextFormat, LegacyColorCodeAlias> aliasesByFormat = new HashMap<>();
    private static final Map<String, LegacyColorCodeAlias> aliasesByStrict = new HashMap<>();
    private static final Map<String, LegacyColorCodeAlias> aliasesByExtended = new HashMap<>();
    
    static
    {
        for (LegacyColorCodeAlias alias : values())
        {
            aliasesByCode.put(alias.character, alias);
            aliasesByFormat.put(alias.format, alias);
            alias.strictAliases.forEach(mutation -> aliasesByStrict.put(mutation, alias));
            alias.extendedAliases.forEach(mutation -> aliasesByExtended.put(mutation, alias));
        }
    }
    
    private final char character;
    private final TextFormat format;
    private final Set<String> strictAliases;
    private final Set<String> extendedAliases;
    
    LegacyColorCodeAlias(char character, TextFormat format, String ... aliases)
    {
        this.character = character;
        this.format = format;
        
        Set<String> strict = permutate(name()).collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> extended = new LinkedHashSet<>(strict);
        
        Arrays.stream(aliases).flatMap(this::permutate).forEach(extended::add);
        
        this.strictAliases = Set.copyOf(strict);
        this.extendedAliases = Set.copyOf(extended);
    }
    
    private Stream<String> permutate(String alias)
    {
        Stream.Builder<String> stream = Stream.builder();
        String lowercase = alias.toLowerCase(Locale.ROOT);
        
        stream.add(lowercase);
        stream.add(lowercase.replace("_", "-"));
        stream.add(lowercase.replace("_", ""));
        
        return stream.build();
    }
    
    public char character() { return character; }
    
    public TextFormat format() { return format; }
    
    public Set<String> strictAliases() { return strictAliases; }
    
    public Set<String> allAliases() { return extendedAliases; }
    
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
    
    public boolean isReset() { return format == Reset.RESET; }
    
    public static Optional<LegacyColorCodeAlias> resolveByCharacter(char code)
    {
        return Optional.ofNullable(aliasesByCode.get(Character.toLowerCase(code)));
    }
    
    public static Optional<LegacyColorCodeAlias> resolveByFormat(TextFormat format)
    {
        return Optional.ofNullable(aliasesByFormat.get(format));
    }
    
    public static LegacyColorCodeAlias of(TextFormat format)
    {
        return resolveByFormat(format).orElseThrow(() ->
            new IllegalArgumentException("Unsupported format: " + format)
        );
    }
    
    private static Optional<LegacyColorCodeAlias> resolveByAlias(Map<String, LegacyColorCodeAlias> map, String alias)
    {
        return Optional.ofNullable(map.get(alias.toLowerCase()));
    }
    
    public static Optional<LegacyColorCodeAlias> resolveByAlias(String alias)
    {
        return resolveByAlias(aliasesByExtended, alias);
    }
    
    public static Optional<LegacyColorCodeAlias> resolveByStrictAlias(String alias)
    {
        return resolveByAlias(aliasesByStrict, alias);
    }
    
    public static final class Reset implements TextFormat
    {
        public static final Reset RESET = new Reset();
        
        private Reset() {}
    }
}
