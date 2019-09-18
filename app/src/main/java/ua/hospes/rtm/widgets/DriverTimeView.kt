package ua.hospes.rtm.widgets

import android.content.Context
import android.util.AttributeSet

import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.TimeUtils

class DriverTimeView : TimeView {
    var session: Session? = null
        set(session) {
            field = session
            if (session != null && session.startDurationTime == -1L) text = TimeUtils.format(0)
        }
    var prevDuration = 0L
        set(prevDuration) {
            field = prevDuration
            text = TimeUtils.formatNano(prevDuration)
        }


    override var currentNanoTime: Long
        get() = super.currentNanoTime
        set(currentNanoTime) {
            super.currentNanoTime = currentNanoTime
            if (this.session == null) return
            text = TimeUtils.formatNano(currentNanoTime - (this.session?.startDurationTime ?: 0L) + this.prevDuration)
        }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}