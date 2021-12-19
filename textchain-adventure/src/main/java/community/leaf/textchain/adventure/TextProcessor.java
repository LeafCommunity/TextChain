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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * Converts a string into a text component.
 */
@FunctionalInterface
public interface TextProcessor
{
    TextComponent process(String text);
    
    @FunctionalInterface
    interface Unprocessed extends TextProcessor {}
    
    @FunctionalInterface
    interface Legacy extends TextProcessor {}
    
    /**
     * Simply creates a text component containing
     * the input string. The text isn't "processed"
     * beyond that.
     *
     * @return  a new component containing the input text
     */
    static Unprocessed none()
    {
        return Component::text;
    }
    
    /**
     * Creates a text component by parsing legacy
     * ampersand-style color codes contained
     * within the input string.
     *
     * @return  a new component as a result of
     *          processing ampersand color codes
     */
    static Legacy legacyAmpersand()
    {
        return LegacyComponentSerializer.legacyAmpersand()::deserialize;
    }
}
