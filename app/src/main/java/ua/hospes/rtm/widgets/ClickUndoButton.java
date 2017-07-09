package ua.hospes.rtm.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Andrew Khloponin
 */
public class ClickUndoButton extends AppCompatButton {
    private boolean undoState = false;
    private long startTime = 0;
    private long endTime = 0;
    private long progressTime = 0;


    public ClickUndoButton(Context context) {
        super(context);
    }

    public ClickUndoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickUndoButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }


    @Override
    public boolean performClick() {
        if (undoState) {
            return performUndoClick();
        } else
            return super.performClick();
    }


    public boolean performUndoClick() {
        return false;
    }

    public interface OnUndoClickListener {
        void onUndoClick();
    }
}