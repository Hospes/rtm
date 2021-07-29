package ua.hospes.rtm.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout
import java.util.*

/**
 * @author Andrew Khloponin
 */
class CheckableFrameLayout : FrameLayout, Checkable {
    private var onCheckedChangeListener: OnCheckedChangeListener? = null
    private var checked: Boolean = false
    private val checkables = ArrayList<Checkable>()


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is Checkable) checkables.add(view as Checkable)
        }
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun setChecked(checked: Boolean) {
        if (this.checked != checked && onCheckedChangeListener != null) onCheckedChangeListener!!.onCheckedChanged(checked)
        this.checked = checked
        for (checkable in checkables) checkable.isChecked = checked
        refreshDrawableState()
    }

    override fun toggle() {
        isChecked = !checked
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CheckedStateSet)
        }
        return drawableState
    }


    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        onCheckedChangeListener = listener
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param isChecked The new checked state of buttonView.
         */
        fun onCheckedChanged(isChecked: Boolean)
    }

    companion object {

        private val CheckedStateSet = intArrayOf(android.R.attr.state_checked)
    }
}