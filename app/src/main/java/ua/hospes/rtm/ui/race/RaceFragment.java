package ua.hospes.rtm.ui.race;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.core.StopWatchFragment;
import ua.hospes.rtm.core.StopWatchService;
import ua.hospes.rtm.domain.preferences.PreferencesManager;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.utils.TimeUtils;
import ua.hospes.rtm.utils.UiUtils;
import ua.hospes.undobutton.UndoButton;
import ua.hospes.undobutton.UndoButtonController;

/**
 * @author Andrew Khloponin
 */
public class RaceFragment extends StopWatchFragment implements RaceContract.View {
    private static final int REQUEST_CODE_PERMISSION = 11;
    private UndoButtonController undoController;
    private TimerListController timerListController;
    @Inject RacePresenter presenter;
    @Inject PreferencesManager preferencesManager;
    private TextView tvTime;
    private RecyclerView rv;
    private RaceAdapter adapter;
    private long currentNanoTime = 0L;


    public static Fragment newInstance() {
        return new RaceFragment();
    }

    public RaceFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    protected int setActionBarTitle() {
        return R.string.race_title;
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

        undoController = new UndoButtonController<RaceAdapter.MyHolder>(getContext()) {
            @Override
            public UndoButton[] provideUndos(RaceAdapter.MyHolder holder) {
                return new UndoButton[]{holder.btnNextSession};
            }

            @Override
            public boolean defaultTimeSHow() {
                return true;
            }

            @Override
            public int defaultDelay() {
                return 5;
            }
        };

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter = new RaceAdapter(getContext(), preferencesManager, undoController));

        adapter.setOnPitClickListener((item, position) -> presenter.onPit(item, currentNanoTime));
        adapter.setOnOutClickListener((item, position) -> presenter.onOut(item, currentNanoTime));
        adapter.setOnUndoClickListener((item, position) -> presenter.undoLastSession(item));
        adapter.setOnSetCarClickListener((item, position) -> presenter.showSetCarDialog(getChildFragmentManager(), item.getSession()));
        adapter.setOnSetDriverClickListener((item, position) -> presenter.showSetDriverDialog(getChildFragmentManager(), item.getSession()));
        adapter.setOnItemClickListener((item, position) -> presenter.showRaceItemDetail(getContext(), item));

        rv.addOnScrollListener(timerListController = new TimerListController());
        rv.addOnScrollListener(undoController);

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
        menu.findItem(R.id.action_add_team).setVisible(!stopWatchStarted);
        menu.findItem(R.id.action_export).setVisible(preferencesManager.isExportXLSEnabled() && !stopWatchStarted);
        menu.findItem(R.id.action_reset).setVisible(!stopWatchStarted);
        menu.findItem(R.id.action_clear).setVisible(!stopWatchStarted);
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
                timerListController.forceUpdate(rv);
                return true;

            case R.id.action_stop:
                StopWatchService.stop(getContext());
                return true;

            case R.id.action_add_team:
                presenter.showAddTeamDialog(getChildFragmentManager());
                return true;

            case R.id.action_export:
                // Assume thisActivity is the current activity
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) presenter.exportXLS();
                else requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                return true;

            case R.id.action_reset:
                presenter.resetRace();
                return true;

            case R.id.action_clear:
                showClearDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.exportXLS();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timerListController.unsubscribe();
        undoController.release();
        presenter.detachView();
    }


    @Override
    public void update(List<RaceItem> items) {
        adapter.clear();
        adapter.addAll(items);
    }


    @Override
    public void onStopWatchStarted(long startTime, long nanoStartTime) {
        presenter.startRace(nanoStartTime);
    }

    @Override
    public void onStopWatchStopped(long stopTime, long nanoStopTime) {
        presenter.stopRace(nanoStopTime);
    }

    @Override
    public void onStopWatchStateChanged(int runningState) {
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onStopWatchTick(long time, long nanoTime, long currentNanoTime) {
        this.currentNanoTime = currentNanoTime;
        // We have to check tvTime on null cause it couldn't be ready yet
        if (tvTime != null) tvTime.setText(TimeUtils.format(time));
        // We have to check adapter on null cause it couldn't be ready yet
        if (timerListController != null) timerListController.updateTime(currentNanoTime);
    }


    private void showClearDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.teams_remove_all)
                .setPositiveButton(R.string.yes, (dialog, which) -> presenter.removeAll())
                .setNegativeButton(R.string.no, (dialog, which) -> {})
                .show();
    }
}