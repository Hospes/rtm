package ua.hospes.rtm.widgets

import android.content.Context
import android.util.AttributeSet

import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.TimeUtils

/**
 * @author Andrew Khloponin
 */
class SessionTimeView : TimeView {
    var session: Session? = null
        set(session) {
            field = session
            if (session == null) return
            if (session.startDurationTime == -1) text = TimeUtils.format(0)
            if (session.startDurationTime != -1 && session.endDurationTime != -1)
                text = TimeUtils.formatNano(session.endDurationTime - session.startDurationTime)
        }


    override var currentNanoTime: Long
        get() = super.currentNanoTime
        set(currentNanoTime) {
            super.currentNanoTime = currentNanoTime
            if (this.session == null) return
            text = TimeUtils.formatNano(currentNanoTime - this.session!!.startDurationTime)
        }


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
}