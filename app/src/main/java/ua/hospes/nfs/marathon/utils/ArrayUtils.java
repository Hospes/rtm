package ua.hospes.nfs.marathon.utils;

import java.util.Arrays;

/**
 * @author Andrew Khloponin
 */
public class ArrayUtils {
    public static String[] convert(int... array) {
        return Arrays.toString(array).replaceAll("[\\[\\]]", "").split("\\s*,\\s*");
    }
}