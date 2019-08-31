package ua.hospes.rtm.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ToggleButton

/**
 * @author Andrew Khloponin
 */
class CustomToggleButton : ToggleButton {
    private val minimumInterval: Long = 5000
    private val previousTimestampClick = System.currentTimeMillis()


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun performClick(): Boolean {
        //        long currentTimestamp = System.currentTimeMillis();

        //        if (currentTimestamp - previousTimestampClick > minimumInterval) {
        //            previousTimestampClick = currentTimestamp;
        return super.performClick()
        //        }
        //        return false;
    }
}