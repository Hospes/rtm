package ua.hospes.nfs.marathon.ui.race.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.utils.TimeUtils;

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
        if (session != null && session.getStartDurationTime() == -1) setText(TimeUtils.format(0));
    }
}