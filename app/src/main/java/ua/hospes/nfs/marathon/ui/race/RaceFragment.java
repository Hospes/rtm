package ua.hospes.nfs.marathon.ui.race;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.StopWatch;
import ua.hospes.nfs.marathon.core.StopWatchFragment;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class RaceFragment extends StopWatchFragment implements RaceContract.View {
    @Inject RacePresenter presenter;
    private TextView tvTime;


    public static Fragment newInstance() {
        return new RaceFragment();
    }

    public RaceFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_race, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTime = UiUtils.findView(view, R.id.time);

        presenter.attachView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }


    @Override
    public void onStopWatchTick(StopWatch stopWatch) {
        tvTime.setText(String.valueOf(stopWatch.getTime()));
    }
}