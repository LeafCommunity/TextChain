/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/TextChain>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.textchain.platforms.bukkit.internal;

import community.leaf.evergreen.bukkit.versions.CraftBukkitVersion;

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
    
    public static PackageResolver inMinecraft() { return in(CraftBukkitVersion.server().minecraftPackage()); }
    
    public static PackageResolver inCraftBukkit() { return in(CraftBukkitVersion.server().craftBukkitPackage()); }
    
    public static ClassResolver on(Class<?> clazz)
    {
        return new ClassResolver(clazz);
    }
    
    public static Optional<MethodHandle> unreflect(Method method)
    {
        try { return Optional.of(LOOKUP.unreflect(method)); }
        catch (IllegalAccessException e) { return Optional.empty(); }
    }
    
    public static RuntimeException safelyRethrow(Throwable throwable)
    {
        if (throwable instanceof Error) { throw (Error) throwable; }
        throw new RuntimeException(throwable);
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
        
        public Optional<MethodHandle> resolveStaticMethod(
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
            return resolveStaticMethod(methodName, returnType, parameterTypes)
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
        
        public Stream<Method> methods() { return Arrays.stream(clazz.getDeclaredMethods()); }
        
        public Stream<Field> fields() { return Arrays.stream(clazz.getDeclaredFields()); }
    }
}
