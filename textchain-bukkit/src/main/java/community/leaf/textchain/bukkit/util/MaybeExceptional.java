package community.leaf.textchain.bukkit.util;

import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class MaybeExceptional<V>
{
    private static final MaybeExceptional<?> EMPTY = new MaybeExceptional<>(null);
    
    @SuppressWarnings("unchecked")
    public static <V> MaybeExceptional<V> empty()
    {
        return (MaybeExceptional<V>) EMPTY;
    }
    
    public static <V> MaybeExceptional<V> of(Supplier<V> supplier)
    {
        try
        {
            @NullOr V value = supplier.get();
            return (value == null) ? empty() : new MaybeExceptional<>(value);
        }
        catch (RuntimeException e) { return new MaybeExceptional<>(e); }
    }
    
    public static <V> MaybeExceptional<V> ofException(Throwable exception)
    {
        return new MaybeExceptional<>(Objects.requireNonNull(exception, "exception"));
    }
    
    private final @NullOr V value;
    private final @NullOr Throwable exception;
    
    private MaybeExceptional(@NullOr V value)
    {
        this.value = value;
        this.exception = null;
    }
    
    private MaybeExceptional(@NullOr Throwable exception)
    {
        this.value = null;
        this.exception = exception;
    }
    
    public V getOrRethrow() throws RuntimeException
    {
        if (exception != null) { throw new RuntimeException(exception); }
        return Objects.requireNonNull(value);
    }
    
    public boolean isExceptional() { return exception != null; }
    
    public Optional<Throwable> getException() { return Optional.ofNullable(exception); }
    
    @SuppressWarnings("unchecked")
    public <T> MaybeExceptional<T> propagate()
    {
        if (exception != null) { return (MaybeExceptional<T>) this; }
        throw new IllegalStateException("Cannot propagate: not exceptional");
    }
    
    public boolean isPresent() { return value != null; }
    
    public Optional<V> toOptional() { return Optional.ofNullable(value); }
}
