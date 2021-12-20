/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.adventure;

final class TextChainImpl extends AbstractTextChain<TextChainImpl>
{
    static final TextChainSource<TextChainImpl> SOURCE = () -> TextChainImpl::new;
    
    TextChainImpl(LinearTextComponentBuilder builder, TextProcessor processor)
    {
        super(builder, processor);
    }
    
    @Override
    public TextChainConstructor<TextChainImpl> constructor()
    {
        return TextChainImpl::new;
    }
}
