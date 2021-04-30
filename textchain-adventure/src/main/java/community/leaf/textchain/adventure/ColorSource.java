/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;

@FunctionalInterface
public interface ColorSource extends RGBLike
{
    TextColor color();
    
    @Override
    default int red() { return color().red(); }
    
    @Override
    default int green() { return color().green(); }
    
    @Override
    default int blue() { return color().blue(); }
}
