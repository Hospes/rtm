package ua.hospes.rtm.ui.race.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * @author Andrew Khloponin
 */
public abstract class TimeView extends AppCompatTextView {
    private long currentNanoTime = 0L;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setCurrentNanoTime(long currentNanoTime) {
        this.currentNanoTime = currentNanoTime;
    }

    public long getCurrentNanoTime() {
        return currentNanoTime;
    }
}