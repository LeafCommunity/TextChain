package community.leaf.textchain.bukkit.util;

import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class ThrowsOr<V>
{
    private static final ThrowsOr<?> EMPTY = new ThrowsOr<>((Object) null);
    
    @SuppressWarnings("unchecked")
    public static <V> ThrowsOr<V> empty()
    {
        return (ThrowsOr<V>) EMPTY;
    }
    
    public static <V> ThrowsOr<V> value(@NullOr V value)
    {
        return (value == null) ? empty() : new ThrowsOr<>(value);
    }
    
    public static <V> ThrowsOr<V> raise(Throwable exception)
    {
        return new ThrowsOr<>(exception);
    }
    
    public static <V> ThrowsOr<V> result(Supplier<V> supplier)
    {
        try { return value(supplier.get()); }
        catch (RuntimeException e) { return raise(e); }
    }
    
    private final @NullOr V value;
    private final @NullOr Throwable exception;
    
    private ThrowsOr(@NullOr V value)
    {
        this.value = value;
        this.exception = null;
    }
    
    private ThrowsOr(Throwable exception)
    {
        this.value = null;
        this.exception = Objects.requireNonNull(exception, "exception");
    }
    
    @SuppressWarnings("ConstantConditions")
    public V getOrThrow() throws RuntimeException
    {
        if (exception != null) { throw new RuntimeException(exception); }
        return Objects.requireNonNull(value, "value");
    }
    
    public boolean isExceptional() { return exception != null; }
    
    public Optional<Throwable> getException() { return Optional.ofNullable(exception); }
    
    @SuppressWarnings("unchecked")
    public <T> ThrowsOr<T> propagate()
    {
        if (exception != null) { return (ThrowsOr<T>) this; }
        throw new IllegalStateException("Cannot propagate: not exceptional");
    }
    
    public boolean isPresent() { return value != null; }
    
    public Optional<V> toOptional() { return Optional.ofNullable(value); }
}
