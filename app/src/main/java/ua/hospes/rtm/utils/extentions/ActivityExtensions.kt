package ua.hospes.rtm.utils.extentions

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment

inline fun <reified T : Any> Activity.extra(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Activity.extraNotNull(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

fun Activity.setLightStatusBar(value: Boolean) {
    val oldFlags = window.decorView.systemUiVisibility
    val flags = when (value) {
        false -> oldFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        true -> oldFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    window.decorView.systemUiVisibility = flags
}