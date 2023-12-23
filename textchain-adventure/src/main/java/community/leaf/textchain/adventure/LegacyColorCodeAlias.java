/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
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

/**
 * Extended aliases for legacy color codes and formatting. 
 */
public enum LegacyColorCodeAlias implements StyleBuilderApplicable
{
    /**
     * Black (<b>color</b>): {@code &0}
     */
    BLACK('0', NamedTextColor.BLACK),
    /**
     * Dark blue (<b>color</b>): {@code &1}
     */
    DARK_BLUE('1', NamedTextColor.DARK_BLUE),
    /**
     * Dark green (<b>color</b>): {@code &2}
     */
    DARK_GREEN('2', NamedTextColor.DARK_GREEN),
    /**
     * Dark aqua (<b>color</b>): {@code &3}
     */
    DARK_AQUA('3', NamedTextColor.DARK_AQUA, "dark_cyan"),
    /**
     * Dark red (<b>color</b>): {@code &4}
     */
    DARK_RED('4', NamedTextColor.DARK_RED),
    /**
     * Dark purple (<b>color</b>): {@code &5}
     */
    DARK_PURPLE('5', NamedTextColor.DARK_PURPLE, "purple"),
    /**
     * Gold (<b>color</b>): {@code &6}
     */
    GOLD('6', NamedTextColor.GOLD, "orange"),
    /**
     * Gray (<b>color</b>): {@code &7}
     */
    GRAY('7', NamedTextColor.GRAY, "light_gray", "light_grey", "grey"),
    /**
     * Dark gray (<b>color</b>): {@code &8}
     */
    DARK_GRAY('8', NamedTextColor.DARK_GRAY, "dark_grey"),
    /**
     * Blue (<b>color</b>): {@code &9}
     */
    BLUE('9', NamedTextColor.BLUE, "violet"),
    /**
     * Green (<b>color</b>): {@code &a}
     */
    GREEN('a', NamedTextColor.GREEN, "light_green", "lime_green", "lime"),
    /**
     * Aqua (<b>color</b>): {@code &b}
     */
    AQUA('b', NamedTextColor.AQUA, "light_aqua", "light_cyan", "cyan"),
    /**
     * Red (<b>color</b>): {@code &c}
     */
    RED('c', NamedTextColor.RED, "light_red"),
    /**
     * Light purple (<b>color</b>): {@code &d}
     */
    LIGHT_PURPLE('d', NamedTextColor.LIGHT_PURPLE, "pink"),
    /**
     * Yellow (<b>color</b>): {@code &e}
     */
    YELLOW('e', NamedTextColor.YELLOW),
    /**
     * White (<b>color</b>): {@code &f}
     */
    WHITE('f', NamedTextColor.WHITE),
    /**
     * Obfuscated (<b>decoration</b>): {@code &k}
     */
    OBFUSCATED('k', TextDecoration.OBFUSCATED, "magic"),
    /**
     * Bold (<b>decoration</b>): {@code &l}
     */
    BOLD('l', TextDecoration.BOLD),
    /**
     * Strikethrough (<b>decoration</b>): {@code &m}
     */
    STRIKETHROUGH('m', TextDecoration.STRIKETHROUGH),
    /**
     * Underline (<b>decoration</b>): {@code &n}
     */
    UNDERLINED('n', TextDecoration.UNDERLINED, "underline"),
    /**
     * Italic (<b>decoration</b>): {@code &o}
     */
    ITALIC('o', TextDecoration.ITALIC),
    /**
     * Reset (<b>style</b>): {@code &r}
     *
     * <p>Unlike every other legacy color code, Adventure does not offer an analogous
     * formatting option for "resetting" the style. Therefore, this is neither a color
     * nor a decoration. Only text chains will recognize this as a valid format.</p>
     */
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
            alias.strictAliases.forEach(permutation -> aliasesByStrict.put(permutation, alias));
            alias.extendedAliases.forEach(permutation -> aliasesByExtended.put(permutation, alias));
        }
    }
    
    private final char character;
    private final TextFormat format;
    private final Set<String> strictAliases;
    private final Set<String> extendedAliases;
    private final Style style;
    
    LegacyColorCodeAlias(char character, TextFormat format, String ... aliases)
    {
        this.character = character;
        this.format = format;
        
        if (format instanceof NamedTextColor)
        {
            this.style = Style.style((NamedTextColor) format);
        }
        else if (format instanceof TextDecoration)
        {
            this.style = Style.style((TextDecoration) format);
        }
        else if (format instanceof Reset)
        {
            this.style = Components.RESET;
        }
        else
        {
            // Shouldn't ever happen, but just in case...
            throw new IllegalArgumentException("Unexpected format: " + format);
        }
        
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
    
    /**
     * The "magical" code used to represent this alias in legacy-formatted strings.
     *
     * @return legacy color code character
     */
    public char character() { return character; }
    
    /**
     * The underlying formatting option that this alias represents.
     *
     * @return a format which may be applied to a component
     */
    public TextFormat format() { return format; }
    
    /**
     * The style that gets applied to components/style-builders when using this alias.
     *
     * @return a style with only this alias' formatting applied
     */
    public Style style() { return style; }
    
    /**
     * This alias' strict alternatives, deriving from the enum value name.
     *
     * @return a set of strict, enum-name-derived aliases
     */
    public Set<String> strictAliases() { return strictAliases; }
    
    /**
     * All alternative aliases for this specific alias, including expanded terminology,
     * synonyms, and additional spellings (like "grey" instead of just "gray").
     *
     * @return a set of all alternative aliases
     */
    public Set<String> allAliases() { return extendedAliases; }
    
    /**
     * Checks if this alias represents a color.
     *
     * @return {@code true} if this alias represents a color
     *
     * @see NamedTextColor
     */
    public boolean isColor() { return format instanceof NamedTextColor; }
    
    /**
     * Gets the color of this alias, or throws an exception if this alias
     * doesn't represent a color.
     *
     * @return a color
     * @throws IllegalStateException if this alias doesn't represent a color
     *
     * @see #isColor()
     * @see #asColor()
     */
    public NamedTextColor color()
    {
        if (isColor()) { return (NamedTextColor) format; }
        throw new IllegalStateException(name() + " is not a color");
    }
    
    /**
     * Gets the color of this alias, or empty if this alias doesn't represent a color.
     *
     * @return a color, otherwise empty
     *
     * @see #isColor()
     */
    public Optional<NamedTextColor> asColor()
    {
        return (isColor()) ? Optional.of((NamedTextColor) format) : Optional.empty();
    }
    
    /**
     * Checks if this alias represents a decoration.
     *
     * @return {@code true} if this alias represents a decoration
     *
     * @see TextDecoration
     */
    public boolean isDecoration() { return format instanceof TextDecoration; }
    
    /**
     * Gets the decoration of this alias, or throws an exception if this alias
     * doesn't represent a decoration.
     *
     * @return a color
     * @throws IllegalStateException if this alias doesn't represent a decoration
     *
     * @see #isDecoration()
     * @see #asDecoration()
     */
    public TextDecoration decoration()
    {
        if (isDecoration()) { return (TextDecoration) format; }
        throw new IllegalStateException(name() + " is not a decoration");
    }
    
    /**
     * Gets the decoration of this alias, or empty if this alias doesn't
     * represent a decoration.
     *
     * @return a decoration, otherwise empty
     *
     * @see #isDecoration()
     */
    public Optional<TextDecoration> asDecoration()
    {
        return (isDecoration()) ? Optional.of((TextDecoration) format) : Optional.empty();
    }
    
    /**
     * Checks if this alias represents 'resetting' all other formatting options.
     *
     * @return {@code true} if this alias is {@link #RESET}
     */
    public boolean isReset() { return format == Reset.RESET; }
    
    @Override
    public void styleApply(Style.Builder builder)
    {
        builder.merge(style);
    }
    
    //
    // Static helpers
    //
    
    /**
     * Attempts to find the legacy alias represented by the provided
     * "magical" code character.
     *
     * @param code  magical color code character
     * @return the legacy alias if found, otherwise empty
     */
    public static Optional<LegacyColorCodeAlias> resolveByCharacter(char code)
    {
        return Optional.ofNullable(aliasesByCode.get(Character.toLowerCase(code)));
    }
    
    /**
     * Attempts to find the corresponding legacy alias of the provided
     * component formatting option.
     *
     * @param format    component formatting option (a color or decoration)
     * @return the legacy alias if found, otherwise empty
     */
    public static Optional<LegacyColorCodeAlias> resolveByFormat(TextFormat format)
    {
        return Optional.ofNullable(aliasesByFormat.get(format));
    }
    
    /**
     * Gets the corresponding legacy alias of the provided component formatting
     * option, or throws an exception if nothing is found.
     *
     * @param format    component formatting option (a color or decoration)
     * @return the legacy alias if found
     * @throws IllegalArgumentException if the format isn't supported
     */
    public static LegacyColorCodeAlias of(TextFormat format)
    {
        return resolveByFormat(format).orElseThrow(() ->
            new IllegalArgumentException("Unsupported format: " + format)
        );
    }
    
    private static Optional<LegacyColorCodeAlias> resolveByAlias(Map<String, LegacyColorCodeAlias> map, String alias)
    {
        return Optional.ofNullable(map.get(alias.toLowerCase(Locale.ROOT)));
    }
    
    /**
     * Attempts to find the legacy alias represented by the provided alias
     * (using all available aliases, including expanded aliases).
     *
     * @param alias     the alias
     * @return the legacy alias if found, otherwise empty
     *
     * @see #allAliases()
     */
    public static Optional<LegacyColorCodeAlias> resolveByAlias(String alias)
    {
        return resolveByAlias(aliasesByExtended, alias);
    }
    
    /**
     * Attempts to find the legacy alias represented by the provided alias
     * (using strictly enum-name-derived aliases).
     *
     * @param alias     the alias
     * @return the legacy alias if found, otherwise empty
     *
     * @see #strictAliases()
     */
    public static Optional<LegacyColorCodeAlias> resolveByStrictAlias(String alias)
    {
        return resolveByAlias(aliasesByStrict, alias);
    }
    
    //
    // Dummy 'reset' text format since Adventure doesn't have one.
    //
    
    /**
     * A dummy formatting option that represents a theoretical text decoration that resets
     * all previously-applied styles. Unfortunately, this isn't useful on its own.
     * You're probably looking for {@link LegacyColorCodeAlias#RESET} instead.
     */
    public static final class Reset implements TextFormat
    {
        /**
         * The constant Reset instance - it does absolutely nothing.
         * You're probably looking for {@link LegacyColorCodeAlias#RESET} instead.
         */
        public static final Reset RESET = new Reset();
        
        private Reset() {}
    }
}
