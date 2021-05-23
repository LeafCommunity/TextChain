/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure.serializers;

import community.leaf.textchain.adventure.LegacyColorAlias;
import community.leaf.textchain.adventure.TextChain;
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

public class ChainSerializers
{
    private ChainSerializers() { throw new UnsupportedOperationException(); }
    
    public static final ChainPlaceholderProcessor NONE = ChainPlaceholderProcessor.strings(Function.identity());
    
    private static <K, V> Optional<V> query(Map<K, V> map, K key) { return Optional.ofNullable(map.get(key)); }
    
    private static <K, V> Optional<String> string(Map<K, V> map, K key)
    {
        return query(map, key).filter(v -> v instanceof String).map(v -> (String) v);
    }
    
    private static void resolveStyleOptionThenApply(TextChain chain, String option)
    {
        Objects.requireNonNull(chain, "chain");
        Objects.requireNonNull(option, "option");
        
        @NullOr LegacyColorAlias alias = LegacyColorAlias.resolveByAlias(option).orElse(null);
        if (alias != null)
        {
            chain.format(alias);
            return;
        }
    
        @NullOr TextColor color = TextColor.fromCSSHexString(option);
        if (color != null)
        {
            chain.color(color);
            return;
        }
    
        //noinspection UnnecessaryReturnStatement
        return; // simply do nothing.
    }
    
    public static TextChain deserializeFromStringMapList(List<Map<String, Object>> maps, ChainPlaceholderProcessor placeholders)
    {
        Objects.requireNonNull(maps, "maps");
        Objects.requireNonNull(placeholders, "placeholders");
        
        TextChain chain = TextChain.chain();
        
        for (Map<String, Object> values : maps)
        {
            String text = string(values, "text").orElse("");
            if (text.isEmpty()) { continue; }
            
            @NullOr Component processed = placeholders.processAsComponent(text.replace("%nl%", "\n"));
            if (processed == null) { continue; }
            chain.then(processed);
            
            string(values, "command").map(placeholders::processAsString).ifPresent(chain::command);
            string(values, "insertion").map(placeholders::processAsString).ifPresent(chain::insertion);
            string(values, "link").map(placeholders::processAsString).ifPresent(chain::link);
            string(values, "suggest").map(placeholders::processAsString).ifPresent(chain::suggest);
            
            string(values, "tooltip")
                .map(tooltip -> tooltip.replace("%nl%", "\n"))
                .map(placeholders::processAsComponent)
                .ifPresent(chain::tooltip);
            
            string(values, "style").ifPresent(style ->
                Arrays.stream(style.split(" ")).forEach(option -> resolveStyleOptionThenApply(chain, option))
            );
        }
        
        return chain;
    }
    
    public static TextChain deserializeFromMapList(List<Map<String, Object>> maps)
    {
        return deserializeFromStringMapList(maps, NONE);
    }
    
    private static Optional<Map<String, Object>> flattenComponentAsStringMap(TextComponent component)
    {
        Objects.requireNonNull(component, "component");
        
        String text = component.content();
        if (text.isEmpty()) { return Optional.empty(); }
        
        Map<String, Object> values = new LinkedHashMap<>();
        
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
    
    public static List<Map<String, Object>> serializeAsMapList(TextComponent component)
    {
        Objects.requireNonNull(component, "component");
        List<Map<String, Object>> maps = new ArrayList<>();
        
        flattenComponentAsStringMap(component).ifPresent(maps::add);
    
        // Handle extras, if they exist
        List<Component> children = component.children();
        if (children.isEmpty()) { return maps; }
        
        for (Component child : children)
        {
            // Flatten extra components recursively and add to list after initial component.
            if (child instanceof TextComponent) { maps.addAll(serializeAsMapList((TextComponent) child)); }
        }
        
        return maps;
    }
    
    public static List<Map<String, Object>> serializeAsMapList(TextChain chain)
    {
        Objects.requireNonNull(chain, "chain");
        return serializeAsMapList(chain.asComponent());
    }
}
