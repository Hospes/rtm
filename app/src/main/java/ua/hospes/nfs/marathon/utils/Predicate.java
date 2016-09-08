package ua.hospes.nfs.marathon.utils;

import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * @author Andrew Khloponin
 */
public class Predicate {
    public static boolean notEmpty(@NonNull Collection<?> collection) {
        return !collection.isEmpty();
    }

    public static Boolean notNull(Object o) {
        return o != null;
    }
}