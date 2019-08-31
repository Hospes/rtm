package ua.hospes.rtm.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ua.hospes.rtm.domain.sessions.Session;
import ua.hospes.rtm.utils.TimeUtils;

/**
 * @author Andrew Khloponin
 */
public class SessionTimeView extends TimeView {
    private Session session;


    public SessionTimeView(Context context) {
        super(context);
    }

    public SessionTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SessionTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setCurrentNanoTime(long currentNanoTime) {
        super.setCurrentNanoTime(currentNanoTime);
        if (session == null) return;
        setText(TimeUtils.formatNano(currentNanoTime - session.getStartDurationTime()));
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
        if (session == null) return;
        if (session.getStartDurationTime() == -1) setText(TimeUtils.format(0));
        if (session.getStartDurationTime() != -1 && session.getEndDurationTime() != -1)
            setText(TimeUtils.formatNano(session.getEndDurationTime() - session.getStartDurationTime()));
    }
}