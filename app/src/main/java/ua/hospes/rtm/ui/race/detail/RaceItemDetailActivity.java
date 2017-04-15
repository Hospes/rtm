package ua.hospes.rtm.ui.race.detail;

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

import dagger.android.AndroidInjection;
import hugo.weaving.DebugLog;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.race.models.DriverDetails;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class RaceItemDetailActivity extends AppCompatActivity implements RaceItemDetailContract.View {
    private final static String KEY_RACE_ITEM_ID = "race_item_id";
    @Inject RaceItemDetailPresenter presenter;
    private int raceItemId = -1;

    private RecyclerView rvDrivers;
    private DriverDetailsAdapter driverDetailsAdapter;

    private SessionAdapter adapter;
    private RecyclerView rvSessions;


    public static void start(Context context, int raceItemId) {
        context.startActivity(new Intent(context, RaceItemDetailActivity.class)
                .putExtra(KEY_RACE_ITEM_ID, raceItemId)
        );
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
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
        }
        presenter.listenRaceItem(raceItemId);

        rvDrivers.setHasFixedSize(true);
        rvDrivers.setNestedScrollingEnabled(true);
        rvDrivers.setLayoutManager(new GridLayoutManager(this, 2));
        rvDrivers.setAdapter(driverDetailsAdapter = new DriverDetailsAdapter());

        rvSessions.setHasFixedSize(true);
        rvSessions.setNestedScrollingEnabled(true);
        rvSessions.setLayoutManager(new LinearLayoutManager(this));
        rvSessions.setAdapter(adapter = new SessionAdapter());
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