package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.function.Function;

public interface ChainPlaceholderProcessor
{
    static ChainPlaceholderProcessor using(
        Function<String, @NullOr String> stringProcessor,
        Function<String, @NullOr Component> componentProcessor
    )
    {
        Objects.requireNonNull(stringProcessor, "stringProcessor");
        Objects.requireNonNull(componentProcessor, "componentProcessor");
        
        return new ChainPlaceholderProcessor()
        {
            @Override
            public @NullOr String processAsString(String input)
            {
                return stringProcessor.apply(input);
            }
    
            @Override
            public @NullOr Component processAsComponent(String input)
            {
                return componentProcessor.apply(input);
            }
        };
    }
    
    static ChainPlaceholderProcessor strings(Function<String, @NullOr String> stringProcessor)
    {
        Objects.requireNonNull(stringProcessor, "stringProcessor");
        
        return new ChainPlaceholderProcessor()
        {
            @Override
            public @NullOr String processAsString(String input)
            {
                return stringProcessor.apply(input);
            }
    
            @Override
            public @NullOr Component processAsComponent(String input)
            {
                @NullOr String text = stringProcessor.apply(input);
                return (text == null) ? null : Component.text(text);
            }
        };
    }
    
    @NullOr String processAsString(String input);
    
    @NullOr Component processAsComponent(String input);
}
