package community.leaf.textchain.adventure;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;

public interface TextChainFactory<T extends TextChain<T>>
{
	TextChainFactory<T> processor(TextProcessor processor);
	
	default TextChainFactory<T> unprocessed()
	{
		return processor(TextProcessor.none());
	}
	
	default TextChainFactory<T> legacy()
	{
		return processor(TextProcessor.legacyAmpersand());
	}
	
	TextChainFactory<T> style(Style style);
	
	default TextChainFactory<T> reset()
	{
		return style(Components.RESET);
	}
	
	TextChainFactory<T> wrap(TextComponent.Builder builder);
	
	default TextChainFactory<T> wrap(ComponentLike componentLike)
	{
		return wrap(Components.safelyAsTextComponentBuilder(componentLike));
	}
	
	T chain();
}
