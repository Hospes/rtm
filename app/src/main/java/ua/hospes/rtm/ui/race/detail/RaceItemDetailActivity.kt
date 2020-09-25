package ua.hospes.rtm.ui.race.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.databinding.ActivityRaceItemDetailBinding
import ua.hospes.rtm.domain.race.models.DriverDetails
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.extentions.extraNotNull

fun Context.intentRaceItemDetails(id: Long) = Intent(this, RaceItemDetailActivity::class.java).apply { putExtra(KEY_ID, id) }

private const val KEY_ID = "key_id"

@AndroidEntryPoint
class RaceItemDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRaceItemDetailBinding.inflate(layoutInflater) }
    private val viewModel: RaceItemDetailViewModel by viewModels()
    private val raceItemId by extraNotNull<Long>(KEY_ID)

    private val driversAdapter = DriverDetailsAdapter()
    private val sessionsAdapter = SessionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.drivers.apply {
            layoutManager = GridLayoutManager(this@RaceItemDetailActivity, 2)
            adapter = driversAdapter
        }
        binding.sessions.apply {
            layoutManager = LinearLayoutManager(this@RaceItemDetailActivity)
            adapter = sessionsAdapter
        }

        viewModel.listenRaceItem().observe(this) { onRaceItem(it) }
        viewModel.listenSessions().observe(this) { onSessions(it) }

        if (savedInstanceState == null) {
            viewModel.init(raceItemId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun onRaceItem(item: RaceItem) {
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

    private fun onSessions(list: List<Session>) = sessionsAdapter.submitList(list)
}