package ua.hospes.nfs.marathon.utils;

import java.util.Locale;

/**
 * @author Andrew Khloponin
 */
public class TimeUtils {
    public static String format(long nanoTime) {
        long second = (nanoTime / 1000) % 60;
        long minute = (nanoTime / (1000 * 60)) % 60;
        long hour = (nanoTime / (1000 * 60 * 60)) % 24;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    }

    public static String formatWithMills(long nanoTime) {
        long millis = nanoTime % 1000;
        long second = (nanoTime / 1000) % 60;
        long minute = (nanoTime / (1000 * 60)) % 60;
        long hour = (nanoTime / (1000 * 60 * 60)) % 24;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hour, minute, second, millis);
    }
}