package ua.hospes.rtm.ui.race.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import ua.hospes.rtm.R
import ua.hospes.rtm.core.DiActivity
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.extentions.extraNotNull
import javax.inject.Inject

fun Context.intentRaceItemDetails(id: Int) = Intent(this, RaceItemDetailActivity::class.java)
        .apply { putExtra(KEY_ID, id) }

private const val KEY_ID = "key_id"

class RaceItemDetailActivity : DiActivity(R.layout.activity_race_item_detail), RaceItemDetailContract.View {
    @Inject lateinit var presenter: RaceItemDetailPresenter
    private val raceItemId by extraNotNull<Int>(KEY_ID)

    private var driverDetailsAdapter: DriverDetailsAdapter? = null
    private var adapter: SessionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //        Toolbar toolbar = findViewById(R.id.toolbar);
        //        setSupportActionBar(toolbar);
        //        getSupportActionBar().setHomeButtonEnabled(true);
        //        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        //        RecyclerView rvDrivers = UiUtils.findView(this, R.id.drivers);
        //        RecyclerView rvSessions = UiUtils.findView(this, R.id.list);
        //
        //        presenter.attachView(this);
        //        if (getIntent() != null) {
        //            raceItemId = getIntent().getIntExtra(KEY_RACE_ITEM_ID, -1);
        //        }
        //        presenter.listenRaceItem(raceItemId);
        //
        //        rvDrivers.setHasFixedSize(true);
        //        rvDrivers.setLayoutManager(new GridLayoutManager(this, 2));
        //        rvDrivers.setAdapter(driverDetailsAdapter = new DriverDetailsAdapter());
        //
        //        rvSessions.setHasFixedSize(true);
        //        rvSessions.setLayoutManager(new LinearLayoutManager(this));
        //        rvSessions.setAdapter(adapter = new SessionAdapter());

        presenter.attachView(this, lifecycle)
    }


    override fun onUpdateRaceItem(item: RaceItem) = Unit
    //        getSupportActionBar().setTitle(raceItem.getTeam().getName());
    //
    //        List<DriverDetails> driverDetailses = new ArrayList<>();
    //        for (Driver driver : raceItem.getTeam().getDrivers()) {
    //            DriverDetails driverDetails = new DriverDetails();
    //
    //            driverDetails.setId(driver.getId());
    //            driverDetails.setName(driver.getName());
    //            driverDetails.setPrevDuration(raceItem.getDetails().getDriverDuration(driver.getId()));
    //            driverDetails.setSession(raceItem.getSession());
    //
    //            driverDetailses.add(driverDetails);
    //        }
    //        driverDetailsAdapter.addAll(driverDetailses);


    override fun onUpdateSessions(list: List<Session>) = Unit
    //        adapter.clear();
    //        adapter.addAll(sessions);

    override fun onError(t: Throwable) = Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
}