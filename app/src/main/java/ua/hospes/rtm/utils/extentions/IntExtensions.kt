package ua.hospes.rtm.utils.extentions

import android.content.res.Resources

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Boolean?.toInt() = if (this == true) 1 else 0