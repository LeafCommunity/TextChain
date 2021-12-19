/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.internal.nms;

import community.leaf.textchain.platforms.ItemRarity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface ItemReflection
{
    static ItemReflection items() { return ItemReflectionImpl.INSTANCE.getOrThrow(); }
    
    String compoundTag(ItemStack item) throws Throwable;
    
    String translationKey(Material type) throws Throwable;
    
    ItemRarity rarity(Material type) throws Throwable;
    
    ItemRarity rarity(ItemStack item) throws Throwable;
}
