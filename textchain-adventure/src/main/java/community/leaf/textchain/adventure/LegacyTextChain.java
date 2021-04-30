/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

import net.kyori.adventure.text.TextComponent;

/**
 * A text chain that will automatically process legacy
 * ampersand-style color codes in text (like {@code "&3&o"}).
 * Appended text is processed using:
 * {@link TextProcessor#legacyAmpersand(String)}.
 * To append text without processing color codes,
 * simply use {@link #thenUnprocessed(String)}.
 */
public final class LegacyTextChain extends Chain<LegacyTextChain>
{
    /**
     * Make a new LegacyTextChain, but
     * {@link TextChain#legacy()} is better.
     */
    public LegacyTextChain(WrappedTextComponentBuilder builder) { super(builder); }
    
    @Override
    public ChainConstructor<LegacyTextChain> getConstructor() { return LegacyTextChain::new; }
    
    @Override
    public TextComponent processText(String text) { return TextProcessor.legacyAmpersand(text); }
}
