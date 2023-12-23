/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms;

import community.leaf.textchain.adventure.ColorSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

/**
 * Represents an item's rarity.
 * This enum is a one-to-one reimplementation of Minecraft's internal {@code net.minecraft.EnumItemRarity},
 * but it uses Adventure colors, components, and isn't restricted to a specific platform.
 */
public enum ItemRarity implements ColorSource, ComponentLike
{
    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.YELLOW),
    RARE(NamedTextColor.AQUA),
    EPIC(NamedTextColor.LIGHT_PURPLE);
    
    private final NamedTextColor color;
    private final TextComponent component;
    
    ItemRarity(NamedTextColor color)
    {
        this.color = color;
        this.component = Component.text(name()).color(color);
    }
    
    /**
     * Gets the color associated with this level of rarity.
     *
     * @return rarity level's color
     */
    @Override
    public NamedTextColor color() { return color; }
    
    @Override
    public Component asComponent() { return component; }
    
    /**
     * Resolves the enum value that matches the case-insensitive input, or else empty.
     *
     * @param name  possible name of an enum value
     *
     * @return the resolved enum value or empty
     */
    public static Optional<ItemRarity> resolveByName(String name)
    {
        for (ItemRarity rarity : values())
        {
            if (rarity.name().equalsIgnoreCase(name)) { return Optional.of(rarity); }
        }
        return Optional.empty();
    }
}

