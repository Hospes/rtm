package ua.hospes.rtm.utils

import java.util.*

object TimeUtils {
    private const val NANO_2_MILLIS = 1000000L

    fun format(millisTime: Long): String {
        val second = millisTime / 1000 % 60
        val minute = millisTime / (1000 * 60) % 60
        val hour = millisTime / (1000 * 60 * 60) % 24

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second)
    }

    fun formatWithMills(millisTime: Long): String {
        val millis = millisTime % 1000
        val second = millisTime / 1000 % 60
        val minute = millisTime / (1000 * 60) % 60
        val hour = millisTime / (1000 * 60 * 60) % 24

        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hour, minute, second, millis)
    }


    fun formatNano(nanoTime: Long): String {
        val millisTime = nanoTime / NANO_2_MILLIS
        val second = millisTime / 1000 % 60
        val minute = millisTime / (1000 * 60) % 60
        val hour = millisTime / (1000 * 60 * 60) % 24

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second)
    }

    fun formatNanoWithMills(nanoTime: Long): String {
        val millisTime = nanoTime / NANO_2_MILLIS
        val millis = millisTime % 1000
        val second = millisTime / 1000 % 60
        val minute = millisTime / (1000 * 60) % 60
        val hour = millisTime / (1000 * 60 * 60) % 24

        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hour, minute, second, millis)
    }
}