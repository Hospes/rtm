package ua.hospes.rtm.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Khloponin
 */
public class CheckableFrameLayout extends FrameLayout implements Checkable {
    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean checked;
    private List<Checkable> checkables = new ArrayList<>();

    private static final int[] CheckedStateSet = {android.R.attr.state_checked};


    public CheckableFrameLayout(Context context) {
        super(context);
    }

    public CheckableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof Checkable) checkables.add((Checkable) view);
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked && onCheckedChangeListener != null) onCheckedChangeListener.onCheckedChanged(checked);
        this.checked = checked;
        for (Checkable checkable : checkables) checkable.setChecked(checked);
        refreshDrawableState();
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }


    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param isChecked The new checked state of buttonView.
         */
        void onCheckedChanged(boolean isChecked);
    }
}