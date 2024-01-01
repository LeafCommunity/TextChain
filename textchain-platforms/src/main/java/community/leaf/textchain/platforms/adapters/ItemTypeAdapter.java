/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.adapters;

import community.leaf.textchain.platforms.ItemRarity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;

public interface ItemTypeAdapter<T>
{
    Key key(T type);
    
    String translationKey(T type);
    
    String clientName(T type);
    
    ItemRarity rarity(T type);
    
    default TranslatableComponent translatable(T type)
    {
        return Component.translatable(translationKey(type));
    }
}
