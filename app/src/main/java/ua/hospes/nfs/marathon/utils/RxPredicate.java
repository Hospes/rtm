package ua.hospes.nfs.marathon.utils;

import java.util.List;

import rx.functions.Func0;
import rx.functions.Func1;

/**
 * @author Andrew Khloponin
 */
public class RxPredicate {
    public static <T> Func1<T, Boolean> nonNull() {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(T o) {
                return Predicate.notNull(o);
            }
        };
    }

    public static Func0<Boolean> nonNull(final Object o) {
        return new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return o != null;
            }
        };
    }

    public static <T> Func1<List<T>, Boolean> notEmpty() {
        return new Func1<List<T>, Boolean>() {
            @Override
            public Boolean call(List<T> collection) {
                return Predicate.notEmpty(collection);
            }
        };
    }
}