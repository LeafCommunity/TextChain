/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ChainSerializer
{
    private ChainSerializer() { throw new UnsupportedOperationException(); }
    
    private static <K, V> Optional<V> query(Map<K, V> map, K key) { return Optional.ofNullable(map.get(key)); }
    
    private static void resolveStyleOptionThenApply(TextChain chain, String option)
    {
        Objects.requireNonNull(chain, "chain");
        Objects.requireNonNull(option, "option");
        
        String upperCaseOption = option.toUpperCase().replace("-", "_");
        
        @NullOr NamedTextColor named = NamedTextColor.NAMES.value(upperCaseOption);
        if (named != null)
        {
            chain.color(named);
            return;
        }
    
        @NullOr TextDecoration decoration = TextDecoration.NAMES.value(upperCaseOption);
        if (decoration != null)
        {
            chain.format(decoration);
            return;
        }
    
        @NullOr TextColor color = TextColor.fromCSSHexString(option);
        if (color != null)
        {
            chain.color(color);
            return;
        }
    }
    
    public static TextChain deserializeFromStringMapList(
        List<Map<String, String>> maps,
        Function<String, String> placeholderProcessor
    )
    {
        Objects.requireNonNull(maps, "maps");
        Objects.requireNonNull(placeholderProcessor, "placeholderProcessor");
        
        TextChain chain = TextChain.chain();
        
        for (Map<String, String> values : maps)
        {
            String text = query(values, "text").orElse("");
            if (text.isEmpty()) { continue; }
            
            chain.then(placeholderProcessor.apply(text.replace("%nl%", "\n")));
            
            query(values, "command").map(placeholderProcessor).ifPresent(chain::command);
            query(values, "insertion").map(placeholderProcessor).ifPresent(chain::insertion);
            query(values, "link").map(placeholderProcessor).ifPresent(chain::link);
            query(values, "suggest").map(placeholderProcessor).ifPresent(chain::suggest);
            
            query(values, "tooltip")
                .map(tooltip -> tooltip.replace("%nl%", "\n"))
                .map(placeholderProcessor)
                .ifPresent(chain::tooltip);
            
            query(values, "style").ifPresent(style ->
                Arrays.stream(style.split(" ")).forEach(option -> resolveStyleOptionThenApply(chain, option))
            );
        }
        
        return chain;
    }
    
    public static TextChain deserializeFromStringMapList(List<Map<String, String>> maps)
    {
        return deserializeFromStringMapList(maps, Function.identity());
    }
    
    private static Optional<Map<String, String>> flattenComponentAsStringMap(TextComponent component)
    {
        Objects.requireNonNull(component, "component");
        
        String text = component.content();
        if (text.isEmpty()) { return Optional.empty(); }
        
        Map<String, String> values = new LinkedHashMap<>();
    
        values.put("text", component.content().replace("\n", "%nl%"));
        
        // Tooltip
        Optional.ofNullable(component.hoverEvent())
            .filter(hover -> hover.action() == HoverEvent.Action.SHOW_TEXT)
            .map(HoverEvent::value)
            .filter(value -> value instanceof Component)
            .map(value -> LegacyComponentSerializer.legacyAmpersand().serialize((Component) value))
            .ifPresent(tooltip -> values.put("tooltip", tooltip));
        
        // Clickables
        @NullOr ClickEvent click = component.clickEvent();
        
        if (click != null && !click.value().isEmpty())
        {
            String clickValue = click.value();
            ClickEvent.Action action = click.action();
            
            if (action == ClickEvent.Action.RUN_COMMAND) { values.put("command", clickValue); }
            else if (action == ClickEvent.Action.OPEN_URL) { values.put("link", clickValue); }
            else if (action == ClickEvent.Action.SUGGEST_COMMAND) { values.put("suggest", clickValue); }
        }
        
        // Insertion
        @NullOr String insertion = component.insertion();
        if (insertion != null && !insertion.isEmpty()) { values.put("insertion", insertion); }
        
        // Styles
        List<String> style = new ArrayList<>();
    
        @NullOr TextColor color = component.color();
        if (color != null)
        {
            NamedTextColor namedColorValue = NamedTextColor.nearestTo(color);
            @NullOr String namedColorName = NamedTextColor.NAMES.key(namedColorValue);
            
            if (namedColorValue.value() == color.value() && namedColorName != null)
            {
                style.add(namedColorName.toLowerCase().replace("_", "-"));
            }
            else
            {
                style.add(color.asHexString());
            }
        }
    
        if (component.hasDecoration(TextDecoration.BOLD)) { style.add("bold"); }
        if (component.hasDecoration(TextDecoration.ITALIC)) { style.add("italic"); }
        if (component.hasDecoration(TextDecoration.OBFUSCATED)) { style.add("obfuscated"); }
        if (component.hasDecoration(TextDecoration.STRIKETHROUGH)) { style.add("strikethrough"); }
        if (component.hasDecoration(TextDecoration.UNDERLINED)) { style.add("underlined"); }
    
        if (style.size() >= 1) { values.put("style", String.join(" ", style)); }
        
        return Optional.of(values);
    }
    
    public static List<Map<String, String>> serializeAsStringMapList(TextComponent component)
    {
        Objects.requireNonNull(component, "component");
        List<Map<String, String>> maps = new ArrayList<>();
        
        flattenComponentAsStringMap(component).ifPresent(maps::add);
    
        // Handle extras, if they exist
        List<Component> children = component.children();
        if (children.isEmpty()) { return maps; }
        
        for (Component child : children)
        {
            // Flatten extra components recursively and add to list after initial component.
            if (child instanceof TextComponent) { maps.addAll(serializeAsStringMapList((TextComponent) child)); }
        }
        
        return maps;
    }
    
    public static List<Map<String, String>> serializeAsStringMapList(TextChain chain)
    {
        Objects.requireNonNull(chain, "chain");
        return serializeAsStringMapList(chain.asComponent());
    }
}
