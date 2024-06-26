package ua.hospes.rtm.core.ui

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class AbsMainFragment : Fragment {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateABTitle(activity, setActionBarTitle())
    }

    @StringRes
    protected abstract fun setActionBarTitle(): Int

    private fun updateABTitle(activity: Activity?, @StringRes title: Int) {
        if (title == -1 || title == 0 || activity !is AppCompatActivity) return

        val ab = activity.supportActionBar
        if (ab != null) ab.title = getString(title)
    }
}