package ua.hospes.rtm.ui.race.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_race_item_detail.*
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.race.models.DriverDetails
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.extentions.extraNotNull

fun Context.intentRaceItemDetails(id: Long) = Intent(this, RaceItemDetailActivity::class.java)
        .apply { putExtra(KEY_ID, id) }

private const val KEY_ID = "key_id"

internal class RaceItemDetailActivity : AppCompatActivity(R.layout.activity_race_item_detail), RaceItemDetailContract.View {
    /*@Inject*/ lateinit var presenter: RaceItemDetailPresenter
    private val raceItemId by extraNotNull<Long>(KEY_ID)

    private val driversAdapter = DriverDetailsAdapter()
    private val sessionsAdapter = SessionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drivers.apply {
            layoutManager = GridLayoutManager(this@RaceItemDetailActivity, 2)
            adapter = driversAdapter
        }
        sessions.apply {
            layoutManager = LinearLayoutManager(this@RaceItemDetailActivity)
            adapter = sessionsAdapter
        }

        presenter.setRaceItemId(raceItemId)
        presenter.attachView(this, lifecycle)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    override fun onRaceItem(item: RaceItem) {
        supportActionBar?.title = item.team.name

        val drivers = mutableListOf<DriverDetails>()
        item.team.drivers.forEach {
            val driverDetails = DriverDetails()

            driverDetails.id = it.id
            driverDetails.name = it.name
            driverDetails.prevDuration = item.details?.getDriverDuration(it.id) ?: 0L
            driverDetails.session = item.session

            drivers.add(driverDetails);
        }

        driversAdapter.submitList(drivers)
    }

    override fun onSessions(list: List<Session>) = sessionsAdapter.submitList(list)

    override fun onError(t: Throwable) = Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
}