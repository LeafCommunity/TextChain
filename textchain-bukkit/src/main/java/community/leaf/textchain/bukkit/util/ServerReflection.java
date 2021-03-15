package community.leaf.textchain.bukkit.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerReflection
{
    private ServerReflection() { throw new UnsupportedOperationException(); }
    
    public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    
    public static Optional<Class<?>> resolveClass(String className)
    {
        try { return Optional.of(Class.forName(className)); }
        catch (ClassNotFoundException ignored) { return Optional.empty(); }
    }
    
    public static Optional<Class<?>> resolveNmsClass(String className)
    {
        return resolveClass(BukkitVersion.getServerVersion().nms(className));
    }
    
    public static Optional<Class<?>> resolveCraftClass(String className)
    {
        return resolveClass(BukkitVersion.getServerVersion().craftbukkit(className));
    }
    
    public static Class<?> requireClass(String className)
    {
        return resolveClass(className).orElseThrow(() -> new IllegalStateException("Missing class: " + className));
    }
    
    public static Class<?> requireNmsClass(String className)
    {
        return requireClass(BukkitVersion.getServerVersion().nms(className));
    }
    
    public static Class<?> requireCraftClass(String className)
    {
        return requireClass(BukkitVersion.getServerVersion().craftbukkit(className));
    }
    
    public static Optional<MethodHandle> resolveMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        try
        {
            return Optional.of(LOOKUP.findVirtual(
                containingClass, methodName, MethodType.methodType(returnType, parameterTypes)
            ));
        }
        catch (NoSuchMethodException | IllegalAccessException ignored) { return Optional.empty(); }
    }
    
    public static Optional<MethodHandle> resolveStaticMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        try
        {
            return Optional.of(LOOKUP.findStatic(
                containingClass, methodName, MethodType.methodType(returnType, parameterTypes)
            ));
        }
        catch (NoSuchMethodException | IllegalAccessException ignored) { return Optional.empty(); }
    }
    
    private static String prettyMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?>[] parameterTypes)
    {
        return containingClass.getCanonicalName() + "." + methodName + "(" +
            Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) +
            ") => " + returnType.getSimpleName();
    }
    
    public static MaybeExceptional<MethodHandle> requireMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        return MaybeExceptional.of(() ->
            resolveMethod(containingClass, methodName, returnType, parameterTypes).orElseThrow(() ->
                new IllegalStateException(
                    "Missing method: " + prettyMethod(containingClass, methodName, returnType, parameterTypes)
                )
            )
        );
    }
    
    public static MaybeExceptional<MethodHandle> requireStaticMethod(Class<?> containingClass, String methodName, Class<?> returnType, Class<?> ... parameterTypes)
    {
        return MaybeExceptional.of(() ->
            resolveStaticMethod(containingClass, methodName, returnType, parameterTypes).orElseThrow(() ->
                new IllegalStateException(
                    "Missing static method: " + prettyMethod(containingClass, methodName, returnType, parameterTypes)
                )
            )
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
    
    public static MaybeExceptional<Field> requireField(Class<?> containingClass, String fieldName, Class<?> expectedType)
    {
        return MaybeExceptional.of(() ->
            resolveField(containingClass, fieldName, expectedType).orElseThrow(() ->
                new IllegalStateException("Missing field: " + prettyField(containingClass, fieldName, expectedType))
            )
        );
    }
    
}
