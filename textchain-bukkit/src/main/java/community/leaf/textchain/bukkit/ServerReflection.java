package community.leaf.textchain.bukkit;

import org.bukkit.Bukkit;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServerReflection
{
    private ServerReflection() { throw new UnsupportedOperationException(); }
    
    public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    
    public static final Version VERSION;
    
    static
    {
        String serverClassName = Bukkit.getServer().getClass().getCanonicalName();
        
        Matcher matcher =
            Pattern.compile("org\\.bukkit\\.craftbukkit\\.(?<version>[^.]+)\\.CraftServer")
                .matcher(serverClassName);
        
        if (!matcher.matches()) { throw new IllegalStateException("Unsupported server: " + serverClassName); }
        
        VERSION = new Version(matcher.group("version"));
    }
    
    public static Optional<Class<?>> resolveClass(String className)
    {
        try { return Optional.of(Class.forName(className)); }
        catch (ClassNotFoundException ignored) { return Optional.empty(); }
    }
    
    public static Class<?> requireClass(String className)
    {
        return resolveClass(className).orElseThrow(() -> new IllegalStateException("Missing class: " + className));
    }
    
    public static Class<?> requireNmsClass(String ... path)
    {
        return requireClass(VERSION.nms(path));
    }
    
    public static Class<?> requireCraftClass(String ... path)
    {
        return requireClass(VERSION.craftbukkit(path));
    }
    
    public static Optional<MethodHandle> resolveMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        try { return Optional.of(LOOKUP.findVirtual(containingClass, methodName, MethodType.methodType(returnType, parameterTypes))); }
        catch (NoSuchMethodException | IllegalAccessException ignored) { return Optional.empty(); }
    }
    
    private static String prettyMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?>[] parameterTypes)
    {
        return containingClass.getCanonicalName() + "." + methodName + "(" +
            Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) +
            ") => " + returnType.getSimpleName();
    }
    
    public static MethodHandle requireMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        return resolveMethod(containingClass, methodName, returnType, parameterTypes).orElseThrow(() ->
            new IllegalStateException("Missing method: " + prettyMethod(containingClass, methodName, returnType, parameterTypes))
        );
    }
    
    public static Optional<MethodHandle> resolveStaticMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        try { return Optional.of(LOOKUP.findStatic(containingClass, methodName, MethodType.methodType(returnType, parameterTypes))); }
        catch (NoSuchMethodException | IllegalAccessException ignored) { return Optional.empty(); }
    }
    
    public static MethodHandle requireStaticMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        return resolveStaticMethod(containingClass, methodName, returnType, parameterTypes).orElseThrow(() ->
            new IllegalStateException("Missing static method: " + prettyMethod(containingClass, methodName, returnType, parameterTypes))
        );
    }
    
    public static Optional<Field> resolveField(Class<?> containingClass, String fieldName, Class<?> expectedType)
    {
        try
        {
            Field field = containingClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            
            return (expectedType.isAssignableFrom(field.getType()))
                ? Optional.of(field)
                : Optional.empty();
        }
        catch (NoSuchFieldException ignored) { return Optional.empty(); }
    }
    
    private static String prettyField(Class<?> containingClass, String fieldName, Class<?> expectedType)
    {
        return containingClass.getCanonicalName() + "." + fieldName + " (" + expectedType.getSimpleName() + ")";
    }
    
    public static Field requireField(Class<?> containingClass, String fieldName, Class<?> expectedType)
    {
        return resolveField(containingClass, fieldName, expectedType).orElseThrow(() ->
            new IllegalStateException("Missing field: " + prettyField(containingClass, fieldName, expectedType))
        );
    }
    
    public static class Version
    {
        public static final Pattern VERSION_PATTERN =
            Pattern.compile("v(?<release>\\d+)_(?<major>\\d+)_R(?<revision>\\d+)");
        
        public final String version;
        public final int release;
        public final int major;
        public final int revision;
        
        public Version(String version)
        {
            this.version = version;
            
            Matcher matcher = VERSION_PATTERN.matcher(version);
            if (!matcher.matches()) { throw new IllegalArgumentException("Invalid version: " + version); }
            
            this.release = Integer.parseInt(matcher.group("release"));
            this.major = Integer.parseInt(matcher.group("major"));
            this.revision = Integer.parseInt(matcher.group("revision"));
        }
        
        @Override
        public String toString() { return version; }
        
        private String prefix(String prefix, String[] path)
        {
            return prefix + "." + version + "." + String.join(".", path);
        }
        
        public String nms(String ... path) { return prefix("net.minecraft.server", path); }
        
        public String craftbukkit(String ... path)  { return prefix("org.bukkit.craftbukkit", path); }
    }
}
