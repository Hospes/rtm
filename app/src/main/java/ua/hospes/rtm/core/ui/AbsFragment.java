package ua.hospes.rtm.core.ui;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class AbsFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateABTitle(getActivity(), setActionBarTitle());
    }

    @StringRes
    protected abstract int setActionBarTitle();

    protected void updateABTitle(Activity activity, @StringRes int title) {
        if (title == -1 || title == 0 || !(activity instanceof AppCompatActivity)) return;

        ActionBar ab = ((AppCompatActivity) activity).getSupportActionBar();
        if (ab != null) ab.setTitle(getString(title));
    }
}