/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters;

import community.leaf.textchain.adventure.LegacyColorCodeAliases;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Optional;

@FunctionalInterface
public interface ColorAdapter<C>
{
    LegacyColorCodeAliases format(C color);
    
    default Optional<NamedTextColor> color(C color)
    {
        return format(color).asColor();
    }
    
    default Optional<TextDecoration> decoration(C color)
    {
        return format(color).asDecoration();
    }
}
