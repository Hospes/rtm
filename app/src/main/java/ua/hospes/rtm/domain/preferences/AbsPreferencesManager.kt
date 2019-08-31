package ua.hospes.rtm.domain.preferences

import android.content.SharedPreferences

abstract class AbsPreferencesManager {
    abstract val preferences: SharedPreferences


    fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putIntSync(key: String, value: Int): Boolean {
        val editor = preferences.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun putFloatSync(key: String, value: Float): Boolean {
        val editor = preferences.edit()
        editor.putFloat(key, value)
        return editor.commit()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun putLongSync(key: String, value: Long): Boolean {
        val editor = preferences.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    fun getString(key: String, defaultValue: String): String? {
        if (!preferences.contains(key)) {
            putString(key, defaultValue)
            return defaultValue
        }
        return preferences.getString(key, defaultValue)
    }

    fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun putStringSync(key: String, value: String): Boolean {
        val editor = preferences.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putBooleanSync(key: String, value: Boolean): Boolean {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    fun remove(key: String) {
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }
}