package ua.hospes.rtm.widgets

import android.content.Context
import android.util.AttributeSet

import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.TimeUtils

class SessionTimeView : TimeView {
    var session: Session? = null
        set(session) {
            field = session
            with(session ?: return) {
                if (startTime == null) text = TimeUtils.formatNano(0L)
                else if (endTime != null) text = TimeUtils.formatNano(endTime - startTime)
            }
        }


    override var currentNanoTime: Long
        get() = super.currentNanoTime
        set(currentNanoTime) {
            super.currentNanoTime = currentNanoTime
            with(session ?: return) {
                text = TimeUtils.formatNano(currentNanoTime - (startTime ?: 0L))
            }
        }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}