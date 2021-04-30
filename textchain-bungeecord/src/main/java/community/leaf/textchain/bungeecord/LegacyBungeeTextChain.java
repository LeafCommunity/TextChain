/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.bungeecord;

import community.leaf.textchain.adventure.ChainConstructor;
import community.leaf.textchain.adventure.TextProcessor;
import community.leaf.textchain.adventure.WrappedTextComponentBuilder;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.TextComponent;

public final class LegacyBungeeTextChain extends BungeeChain<LegacyBungeeTextChain>
{
    public LegacyBungeeTextChain(WrappedTextComponentBuilder builder, BungeeAudiences audiences)
    {
        super(builder, audiences);
    }
    
    @Override
    public ChainConstructor<LegacyBungeeTextChain> getConstructor()
    {
        return builder -> new LegacyBungeeTextChain(builder, adventure());
    }
    
    @Override
    public TextComponent processText(String text)
    {
        return TextProcessor.legacyAmpersand(text);
    }
}
