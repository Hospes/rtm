package ua.hospes.rtm.utils

import android.app.Activity
import android.view.View

object UiUtils {
    fun <T> findView(root: View?, id: Int): T? {
        if (root == null) return null
        try {
            return root.findViewById<View>(id) as T
        } catch (e: ClassCastException) {
            return null
        }

    }

    fun <T> findView(root: View?, tag: Any): T? {
        if (root == null) return null
        try {
            return root.findViewWithTag<View>(tag) as T
        } catch (e: ClassCastException) {
            return null
        }

    }

    fun <T> findView(root: Activity?, id: Int): T? {
        if (root == null) return null
        try {
            return root.findViewById<View>(id) as T
        } catch (e: ClassCastException) {
            return null
        }

    }
}