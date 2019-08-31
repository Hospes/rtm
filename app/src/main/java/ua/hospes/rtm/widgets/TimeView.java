package ua.hospes.rtm.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

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