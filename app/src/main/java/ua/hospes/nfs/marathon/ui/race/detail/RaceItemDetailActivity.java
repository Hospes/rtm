package ua.hospes.nfs.marathon.ui.race.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import hugo.weaving.DebugLog;
import ua.hospes.nfs.marathon.App;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.di.HasComponent;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.core.di.module.ActivityModule;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.race.models.DriverDetails;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
@AutoComponent(dependencies = App.class, modules = {ActivityModule.class})
@AutoInjector
public class RaceItemDetailActivity extends AppCompatActivity implements HasComponent<RaceItemDetailActivityComponent>, RaceItemDetailContract.View {
    private final static String KEY_RACE_ITEM_ID = "race_item_id";
    private final static String KEY_RACE_START_TIME = "race_start_time";
    private RaceItemDetailActivityComponent component;
    @Inject RaceItemDetailPresenter presenter;
    private int raceItemId = -1;

    private RecyclerView rvDrivers;
    private DriverDetailsAdapter driverDetailsAdapter;

    private SessionAdapter adapter;
    private RecyclerView rvSessions;

    private long startTime = -1;


    public static void start(Context context, int raceItemId, long startTime) {
        context.startActivity(new Intent(context, RaceItemDetailActivity.class)
                .putExtra(KEY_RACE_ITEM_ID, raceItemId)
                .putExtra(KEY_RACE_START_TIME, startTime)
        );
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getComponent(this, RaceItemDetailActivityComponent.class).inject(this);

        setContentView(R.layout.activity_race_item_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvDrivers = UiUtils.findView(this, R.id.drivers);
        rvSessions = UiUtils.findView(this, R.id.list);

        presenter.attachView(this);
        if (getIntent() != null) {
            raceItemId = getIntent().getIntExtra(KEY_RACE_ITEM_ID, -1);
            startTime = getIntent().getLongExtra(KEY_RACE_START_TIME, -1);
        }
        presenter.listenRaceItem(raceItemId);

        rvDrivers.setHasFixedSize(true);
        rvDrivers.setNestedScrollingEnabled(true);
        rvDrivers.setLayoutManager(new GridLayoutManager(this, 2));
        rvDrivers.setAdapter(driverDetailsAdapter = new DriverDetailsAdapter());

        rvSessions.setHasFixedSize(true);
        rvSessions.setNestedScrollingEnabled(true);
        rvSessions.setLayoutManager(new LinearLayoutManager(this));
        rvSessions.setAdapter(adapter = new SessionAdapter(startTime));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public RaceItemDetailActivityComponent getComponent() {
        if (component == null) {
            component = DaggerRaceItemDetailActivityComponent.builder()
                    .appComponent(App.get(this).getComponent())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return component;
    }

    @DebugLog
    @Override
    public void onUpdateRaceItem(RaceItem raceItem) {
        getSupportActionBar().setTitle(raceItem.getTeam().getName());

        List<DriverDetails> driverDetailses = new ArrayList<>();
        for (Driver driver : raceItem.getTeam().getDrivers()) {
            DriverDetails driverDetails = new DriverDetails();

            driverDetails.setId(driver.getId());
            driverDetails.setName(driver.getName());
            driverDetails.setPrevDuration(raceItem.getDetails().getDriverDuration(driver.getId()));
            driverDetails.setSession(raceItem.getSession());

            driverDetailses.add(driverDetails);
        }
        driverDetailsAdapter.addAll(driverDetailses);
    }

    @Override
    public void onUpdateSessions(List<Session> sessions) {
        adapter.clear();
        adapter.addAll(sessions);
    }
}