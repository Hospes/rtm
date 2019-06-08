package ua.hospes.rtm.utils.extentions

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

fun isAtLeastMarshmallow() = VERSION.SDK_INT >= VERSION_CODES.M

@Suppress("unused")
fun isAtLeastNougat() = VERSION.SDK_INT >= VERSION_CODES.N