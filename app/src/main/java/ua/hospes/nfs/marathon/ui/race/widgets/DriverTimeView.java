package ua.hospes.nfs.marathon.ui.race.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.utils.TimeUtils;

/**
 * @author Andrew Khloponin
 */
public class DriverTimeView extends TimeView {
    private Session session;
    private long prevDuration = 0L;


    public DriverTimeView(Context context) {
        super(context);
    }

    public DriverTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DriverTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setCurrentNanoTime(long currentNanoTime) {
        super.setCurrentNanoTime(currentNanoTime);
        if (session == null) return;
        setText(TimeUtils.formatNano(currentNanoTime - session.getStartDurationTime() + prevDuration));
    }

    public long getPrevDuration() {
        return prevDuration;
    }

    public void setPrevDuration(long prevDuration) {
        this.prevDuration = prevDuration;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
        if (session != null && session.getStartDurationTime() == -1) setText(TimeUtils.format(0));
    }
}