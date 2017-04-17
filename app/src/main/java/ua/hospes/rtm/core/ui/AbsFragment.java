package ua.hospes.rtm.core.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Andrew Khloponin
 */
public abstract class AbsFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateABTitle(setActionBarTitle());
    }

    @StringRes
    protected abstract int setActionBarTitle();

    protected final void updateABTitle(@StringRes int title) {
        if (title == -1) return;

        if (getActivity() instanceof AppCompatActivity) {
            ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (ab != null) ab.setTitle(title);
        } else {
            android.app.ActionBar ab = getActivity().getActionBar();
            if (ab != null) ab.setTitle(title);
        }
    }
}