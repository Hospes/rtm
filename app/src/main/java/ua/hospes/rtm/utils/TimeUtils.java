package ua.hospes.rtm.utils;

import java.util.Locale;

/**
 * @author Andrew Khloponin
 */
public class TimeUtils {
    private static final long NANO_2_MILLIS = 1000000L;

    public static String format(long millisTime) {
        long second = (millisTime / 1000) % 60;
        long minute = (millisTime / (1000 * 60)) % 60;
        long hour = (millisTime / (1000 * 60 * 60)) % 24;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    }

    public static String formatWithMills(long millisTime) {
        long millis = millisTime % 1000;
        long second = (millisTime / 1000) % 60;
        long minute = (millisTime / (1000 * 60)) % 60;
        long hour = (millisTime / (1000 * 60 * 60)) % 24;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hour, minute, second, millis);
    }


    public static String formatNano(long nanoTime) {
        long millisTime = nanoTime / NANO_2_MILLIS;
        long second = (millisTime / 1000) % 60;
        long minute = (millisTime / (1000 * 60)) % 60;
        long hour = (millisTime / (1000 * 60 * 60)) % 24;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    }

    public static String formatNanoWithMills(long nanoTime) {
        long millisTime = nanoTime / NANO_2_MILLIS;
        long millis = millisTime % 1000;
        long second = (millisTime / 1000) % 60;
        long minute = (millisTime / (1000 * 60)) % 60;
        long hour = (millisTime / (1000 * 60 * 60)) % 24;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hour, minute, second, millis);
    }
}