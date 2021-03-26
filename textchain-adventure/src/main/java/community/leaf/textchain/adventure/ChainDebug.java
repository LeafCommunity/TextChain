package community.leaf.textchain.adventure;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.logging.Logger;

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
