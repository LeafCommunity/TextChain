/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure.internal;

import community.leaf.textchain.adventure.Components;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.logging.Logger;

//  This class exists for testing purposes only.
//  Calls to it should *not* be left in committed code.

@Deprecated
public class ChainDebug
{
    private ChainDebug() { throw new UnsupportedOperationException(); }
    
    private static final Logger LOGGER = Logger.getLogger(ChainDebug.class.getName());
    private static final boolean ENABLED = Boolean.getBoolean("commun".concat("ity.leaf.textchain.adventure.debug"));
    
    private static final String NOT_ENABLED_MESSAGE =
        "Debugging is not enabled! This code was left here by mistake - please report it at: " +
        "https://github.com/LeafCommunity/TextChain/issues";
    
    private static void onlyIfEnabled()
    {
        if (!ENABLED) { throw new IllegalStateException(NOT_ENABLED_MESSAGE); }
    }
    
    @Deprecated
    public static void debug(String message)
    {
        onlyIfEnabled();
        LOGGER.info(message);
    }
    
    @Deprecated
    public static void debug(String message, ComponentLike componentLike)
    {
        onlyIfEnabled();
        LOGGER.info(message);
        LOGGER.info(GsonComponentSerializer.gson().serialize(Components.safelyAsComponent(componentLike)));
    }
}
