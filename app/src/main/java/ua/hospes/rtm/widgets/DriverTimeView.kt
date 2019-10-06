package ua.hospes.rtm.widgets

import android.content.Context
import android.util.AttributeSet

import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.TimeUtils

class DriverTimeView : TimeView {
    var session: Session? = null
        set(session) {
            field = session
            val sessionDuration = with(session ?: return) {
                when {
                    startTime == null -> 0L
                    endTime != null -> endTime - startTime
                    else -> 0L
                }
            }

            text = TimeUtils.formatNano(sessionDuration + prevDuration)
        }
    var prevDuration = 0L
        set(prevDuration) {
            field = prevDuration
            val sessionDuration = with(session ?: return) {
                when {
                    startTime == null -> 0L
                    endTime != null -> endTime - startTime
                    else -> 0L
                }
            }

            text = TimeUtils.formatNano(sessionDuration + prevDuration)
        }


    override var currentNanoTime: Long
        get() = super.currentNanoTime
        set(currentNanoTime) {
            super.currentNanoTime = currentNanoTime
            with(session ?: return) {
                text = TimeUtils.formatNano(currentNanoTime - (startTime ?: 0L) + prevDuration)
            }
        }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}