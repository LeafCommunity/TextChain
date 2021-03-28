package community.leaf.textchain.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Components
{
    private Components() { throw new UnsupportedOperationException(); }
    
    public static final Style UNFORMATTED =
        Style.style()
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .build();
    
    public static Component safelyAsComponent(ComponentLike componentLike)
    {
        Objects.requireNonNull(componentLike, "componentLike");
        return Objects.requireNonNull(componentLike.asComponent(), "componentLike returned null");
    }
    
    public static List<Component> flattenExtra(Component component)
    {
        Objects.requireNonNull(component, "component");
        List<Component> flattened = new ArrayList<>();
        for (Component extra : component.children()) { flattened.add(extra.style(component.style())); }
        return flattened;
    }
    
    public static List<Component> flattenExtraSplitByNewLine(Component component)
    {
        Objects.requireNonNull(component, "component");
        
        List<Component> flattened = new ArrayList<>();
        TextComponent.@NullOr Builder builder = null;
        
        for (Component extra : component.children())
        {
            if (Component.newline().equals(extra))
            {
                if (builder != null)
                {
                    flattened.add(builder.build());
                    builder = null;
                }
                
                continue;
            }
            
            if (builder == null) { builder = Component.text().style(component.style()); }
            builder.append(extra);
        }
        
        if (builder != null) { flattened.add(builder.build()); }
        return flattened;
    }
}
