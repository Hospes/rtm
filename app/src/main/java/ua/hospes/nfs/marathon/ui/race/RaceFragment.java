package ua.hospes.nfs.marathon.ui.race;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.StopWatch;
import ua.hospes.nfs.marathon.core.StopWatchFragment;
import ua.hospes.nfs.marathon.core.StopWatchService;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.TimeUtils;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class RaceFragment extends StopWatchFragment implements RaceContract.View {
    @Inject RacePresenter presenter;
    private TextView tvTime;
    private RecyclerView rv;
    private RaceAdapter adapter;


    public static Fragment newInstance() {
        return new RaceFragment();
    }

    public RaceFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_race, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = UiUtils.findView(view, R.id.list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter = new RaceAdapter());
        //adapter.setOnItemClickListener((item, position) -> showEditTeamDialog(item));

        presenter.attachView(this);
    }


    //region ActionBar Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.race, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        boolean stopWatchStarted = isStopWatchStarted();
        menu.findItem(R.id.action_start).setVisible(!stopWatchStarted);
        menu.findItem(R.id.action_stop).setVisible(stopWatchStarted);

        MenuItem stopWatchItem = menu.findItem(R.id.action_stopwatch);
        if (stopWatchItem != null) {
            tvTime = (TextView) MenuItemCompat.getActionView(stopWatchItem).findViewById(R.id.text);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                StopWatchService.start(getContext());
                return true;

            case R.id.action_stop:
                StopWatchService.stop(getContext());
                return true;

            case R.id.action_settings:
                presenter.showAddTeamDialog(getChildFragmentManager());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }


    @Override
    public void onStopWatchStateChanged(StopWatch stopWatch) {
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onStopWatchTick(StopWatch stopWatch) {
        tvTime.setText(TimeUtils.format(stopWatch.getTime()));
    }


    @Override
    public void update(List<RaceItem> items) {
        adapter.clear();
        adapter.addAll(items);
    }
}