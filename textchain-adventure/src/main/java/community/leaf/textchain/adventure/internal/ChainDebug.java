/*
 * Copyright © 2021-2023, RezzedUp <https://github.com/LeafCommunity/TextChain>
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

@Deprecated(forRemoval = true)
public class ChainDebug
{
    private ChainDebug() { throw new UnsupportedOperationException(); }
    
    private static final Logger LOGGER = Logger.getLogger(ChainDebug.class.getName());
    
    // Enable debugging with: -Dcommunity.leaf.textchain.debug=true
    private static final boolean ENABLED =
        Boolean.getBoolean("commun".concat("ity.le").concat("af.textchain.debug"));
    
    private static void onlyIfEnabled()
    {
        if (!ENABLED)
        {
            throw new IllegalStateException(
                "Debugging is not enabled! This code was left here by mistake - please report it at: " +
                "https://github.com/LeafCommunity/TextChain/issues"
            );
        }
    }
    
    @Deprecated(forRemoval = true)
    public static void debug(String message)
    {
        onlyIfEnabled();
        LOGGER.info(message);
    }
    
    @Deprecated(forRemoval = true)
    public static void debug(String message, ComponentLike componentLike)
    {
        onlyIfEnabled();
        LOGGER.info(message);
        LOGGER.info(GsonComponentSerializer.gson().serialize(Components.safelyAsComponent(componentLike)));
    }
}
