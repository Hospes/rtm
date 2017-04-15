package ua.hospes.rtm.ui.race.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * @author Andrew Khloponin
 */
public class CustomToggleButton extends ToggleButton {
    private final long minimumInterval = 5000;
    private long previousTimestampClick = System.currentTimeMillis();


    public CustomToggleButton(Context context) {
        super(context);
    }

    public CustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomToggleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean performClick() {
//        long currentTimestamp = System.currentTimeMillis();

//        if (currentTimestamp - previousTimestampClick > minimumInterval) {
//            previousTimestampClick = currentTimestamp;
            return super.performClick();
//        }
//        return false;
    }
}