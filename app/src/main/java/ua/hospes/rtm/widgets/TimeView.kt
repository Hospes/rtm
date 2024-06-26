package ua.hospes.rtm.widgets

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatTextView

abstract class TimeView : AppCompatTextView {
    open var currentNanoTime = 0L

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}