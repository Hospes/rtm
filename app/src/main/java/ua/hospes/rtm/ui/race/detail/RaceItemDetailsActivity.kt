package ua.hospes.rtm.ui.race.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.ActivityRaceItemDetailBinding
import ua.hospes.rtm.domain.race.models.DriverDetails
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session

fun Context.intentRaceItemDetails(id: Long) = Intent(this, RaceItemDetailActivity::class.java)
    .apply { putExtra("race_team_id", id) }

@AndroidEntryPoint
class RaceItemDetailActivity : AppCompatActivity(R.layout.activity_race_item_detail) {
    private val binding by viewBinding(ActivityRaceItemDetailBinding::bind)
    private val viewModel: RaceItemDetailsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val driversAdapter = DriverDetailsAdapter()
        binding.drivers.apply {
            layoutManager = GridLayoutManager(this@RaceItemDetailActivity, 2)
            adapter = driversAdapter
        }
        val sessionsAdapter = SessionAdapter()
        binding.sessions.apply {
            layoutManager = LinearLayoutManager(this@RaceItemDetailActivity)
            adapter = sessionsAdapter
        }

        viewModel.listenRaceItem().observe(this) { onRaceItem(driversAdapter, it) }
        viewModel.listenSessions().observe(this) { onSessions(sessionsAdapter, it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun onRaceItem(adapter: DriverDetailsAdapter, item: RaceItem) {
        supportActionBar?.title = item.team.name

        val drivers = item.team.drivers.map {
            val driverDetails = DriverDetails()

            driverDetails.id = it.id
            driverDetails.name = it.name
            driverDetails.prevDuration = item.details?.getDriverDuration(it.id) ?: 0L
            driverDetails.session = item.session

            driverDetails
        }

        adapter.submitList(drivers)
    }

    private fun onSessions(adapter: SessionAdapter, list: List<Session>) = adapter.submitList(list)
}