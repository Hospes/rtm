package ua.hospes.rtm.utils;

import androidx.annotation.Nullable;

/**
 * @author Andrew Khloponin
 */
public class Optional<T> {
    @Nullable
    private T t = null;


    private Optional() {}


    public static <T> Optional<T> empty() {
        return new Optional<>();
    }

    public static <T> Optional<T> of(T value) {
        Optional<T> o = new Optional<>();
        o.t = value;
        return o;
    }


    @Nullable
    public T get() {
        return t;
    }

    public boolean isPresent() {
        return t != null;
    }
}