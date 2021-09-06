package community.leaf.textchain.bukkit.internal;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Reflect
{
    private Reflect() { throw new UnsupportedOperationException(); }
    
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    
    public static PackageResolver in(String packageName)
    {
        return new PackageResolver(packageName);
    }
    
    public static PackageResolver inMinecraft() { return in(BukkitVersion.server().minecraftPackage()); }
    
    public static PackageResolver inCraftBukkit() { return in(BukkitVersion.server().craftBukkitPackage()); }
    
    public static ClassResolver on(Class<?> clazz)
    {
        return new ClassResolver(clazz);
    }
    
    public static Optional<MethodHandle> unreflect(Method method)
    {
        try { return Optional.of(LOOKUP.unreflect(method)); }
        catch (IllegalAccessException e) { return Optional.empty(); }
    }
    
    public static final class PackageResolver
    {
        private static Stream<Class<?>> resolve(String className)
        {
            try { return Stream.of(Class.forName(className)); }
            catch (ClassNotFoundException ignored) { return Stream.empty(); }
        }
        
        private final String packageName;
        
        private PackageResolver(String packageName)
        {
            this.packageName = Objects.requireNonNull(packageName, "packageName");
        }
        
        public String packageName() { return packageName; }
        
        public String repackage(String className) { return packageName + "." + className; }
        
        public Optional<Class<?>> resolveAnyClass(String ... classNames)
        {
            return Arrays.stream(classNames).map(this::repackage).flatMap(PackageResolver::resolve).findFirst();
        }
        
        public Class<?> requireAnyClass(String ... classNames)
        {
            return resolveAnyClass(classNames).orElseThrow(() -> new IllegalStateException(
                "Could not resolve any classes matching: " +
                Arrays.stream(classNames).map(this::repackage).collect(Collectors.joining(", "))
            ));
        }
    }
    
    public static final class ClassResolver
    {
        private final Class<?> clazz;
        
        public ClassResolver(Class<?> clazz)
        {
            this.clazz = Objects.requireNonNull(clazz, "clazz");
        }
        
        public Class<?> clazz() { return clazz; }
        
        public Optional<MethodHandle> resolveMethod(
            String methodName,
            Class<?> returnType,
            Class<?> ... parameterTypes
        ) {
            try
            {
                return Optional.of(LOOKUP.findVirtual(
                    clazz, methodName, MethodType.methodType(returnType, parameterTypes)
                ));
            }
            catch (NoSuchMethodException | IllegalAccessException ignored) { return Optional.empty(); }
        }
        
        public Optional<MethodHandle> resolveExactStaticMethod(
            String methodName,
            Class<?> returnType,
            Class<?> ... parameterTypes
        ) {
            try
            {
                return Optional.of(LOOKUP.findStatic(
                    clazz, methodName, MethodType.methodType(returnType, parameterTypes)
                ));
            }
            catch (NoSuchMethodException | IllegalAccessException ignored) { return Optional.empty(); }
        }
        
        private String prettyMethod(String methodName, Class<?> returnType, Class<?>[] parameterTypes)
        {
            String className = clazz.getCanonicalName();
            String arguments = Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", "));
            String returns = returnType.getSimpleName();
            
            return className + "." + methodName + "(" + arguments + ") => " + returns;
        }
        
        public ThrowsOr<MethodHandle> requireMethod(String methodName, Class<?> returnType, Class<?> ... parameterTypes)
        {
            return resolveMethod(methodName, returnType, parameterTypes)
                .map(ThrowsOr::value)
                .orElseGet(() ->
                    ThrowsOr.raise(new IllegalStateException(
                        "Missing method: " + prettyMethod(methodName, returnType, parameterTypes)
                    ))
                );
        }
        
        public ThrowsOr<MethodHandle> requireStaticMethod(String methodName, Class<?> returnType, Class<?> ... parameterTypes)
        {
            return resolveExactStaticMethod(methodName, returnType, parameterTypes)
                .map(ThrowsOr::value)
                .orElseGet(() ->
                    ThrowsOr.raise(new IllegalStateException(
                        "Missing static method: " + prettyMethod(methodName, returnType, parameterTypes)
                    ))
                );
        }
        
        public Optional<Field> resolveField(String fieldName, Class<?> expectedType)
        {
            try
            {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                
                return (expectedType.isAssignableFrom(field.getType()))
                    ? Optional.of(field)
                    : Optional.empty();
            }
            catch (NoSuchFieldException ignored) { return Optional.empty(); }
        }
        
        private String prettyField(String fieldName, Class<?> expectedType)
        {
            return clazz.getCanonicalName() + "." + fieldName + " => " + expectedType.getSimpleName();
        }
        
        public ThrowsOr<Field> requireField(String fieldName, Class<?> expectedType)
        {
            return resolveField(fieldName, expectedType)
                .map(ThrowsOr::value)
                .orElseGet(() ->
                    ThrowsOr.raise(new IllegalStateException(
                        "Missing field: " + prettyField(fieldName, expectedType)
                    ))
                );
        }
    }
}
