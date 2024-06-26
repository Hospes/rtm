package ua.hospes.rtm.ui.race

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import ua.hospes.rtm.R
import ua.hospes.rtm.core.StopWatchFragment
import ua.hospes.rtm.core.StopWatchService
import ua.hospes.rtm.databinding.FragmentRaceBinding
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.ui.race.detail.intentRaceItemDetails
import ua.hospes.rtm.utils.TimeUtils
import javax.inject.Inject

private const val REQUEST_CODE_PERMISSION = 11

@AndroidEntryPoint
class RaceFragment : StopWatchFragment(R.layout.fragment_race) {
    //    private lateinit var undoController: UndoButtonController<*>
    private lateinit var timerListController: TimerListController
    private val viewModel: RaceViewModel by viewModels()
    private val binding by viewBinding(FragmentRaceBinding::bind)
    @Inject lateinit var preferencesManager: PreferencesManager
    private var tvTime: TextView? = null
    private var currentNanoTime = 0L

    private val sessionButtonType: String by lazy { preferencesManager.sessionButtonType }
    private val adapter: RaceAdapter by lazy { RaceAdapter(requireContext(), sessionButtonType/*, undoController*/) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.race_title

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        undoController = object : UndoButtonController<RaceAdapter.MyHolderUndoNext>(requireContext()) {
        //            override fun provideUndos(holder: RaceAdapter.MyHolderUndoNext) = arrayOf(holder.btnNextSession)
        //
        //            override fun defaultTimeSHow(): Boolean = true
        //
        //            override fun defaultDelay(): Int = 15
        //        }

        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        binding.list.itemAnimator = null

        adapter.onPitClickListener = { viewModel.onPit(it, currentNanoTime) }
        adapter.onOutClickListener = { viewModel.onOut(it, currentNanoTime) }
        adapter.onUndoClickListener = { viewModel.undoLastSession(it) }
        adapter.setCarClickListener = { viewModel.clickSetCar(it) }
        adapter.setDriverClickListener = { viewModel.clickSetDriver(it) }
        adapter.itemClickListener = { startActivity(requireContext().intentRaceItemDetails(it.id)) }

        timerListController = TimerListController(lifecycleScope).apply { binding.list.addOnScrollListener(this) }

        //        if ("undo".equals(sessionButtonType, ignoreCase = true))
        //            list.addOnScrollListener(undoController)

        viewModel.race.observe(viewLifecycleOwner) { onData(it) }
        viewModel.uiEvents.observe(viewLifecycleOwner) {
            when (it) {
                is RaceViewModel.UIEvent.SetCar -> openSetCarDialog(it.id, it.cars)
                is RaceViewModel.UIEvent.SetDriver -> openSetDriverDialog(it.id, it.drivers)
            }
        }
    }


    //region ActionBar Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.race, menu)

    override fun onPrepareOptionsMenu(menu: Menu) {
        val stopWatchStarted = isStopWatchStarted
        menu.findItem(R.id.action_start).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_add_team).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_export).isVisible = preferencesManager.isExportXLSEnabled && !stopWatchStarted
        menu.findItem(R.id.action_reset).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_clear).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_stop).isVisible = stopWatchStarted

        tvTime = menu.findItem(R.id.action_stopwatch)?.actionView?.findViewById(R.id.text)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_start -> {
            StopWatchService.start(requireContext())
            timerListController.forceUpdate(binding.list)
            true
        }

        R.id.action_stop -> StopWatchService.stop(requireContext()).let { true }
        R.id.action_add_team -> AddTeamToRaceDialogFragment().show(childFragmentManager, "add_team_to_race").let { true }

        R.id.action_export -> {
            // Assume thisActivity is the current activity
            val permissionCheck = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED)
                viewModel.exportXLS()
            else
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
            true
        }

        R.id.action_reset -> viewModel.resetRace().let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }
    //endregion

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) = when (requestCode) {
        REQUEST_CODE_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.exportXLS()
        } else Unit

        else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //        undoController.release()
    }

    private fun onData(items: List<RaceItem>) {
        Timber.d(items.toString())
        adapter.submitList(items)
        timerListController.forceUpdate(binding.list)
        //        if ("undo".equals(sessionButtonType, ignoreCase = true))
        //            undoController.forceUpdate(list)
    }

    // override fun onError(t: Throwable) = Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()


    override fun onStopWatchStarted(startTime: Long, nanoStartTime: Long) = viewModel.startRace(nanoStartTime).let { Unit }
    override fun onStopWatchStopped(stopTime: Long, nanoStopTime: Long) = viewModel.stopRace(nanoStopTime).let { Unit }
    override fun onStopWatchStateChanged(runningState: Int) = activity?.invalidateOptionsMenu() ?: Unit

    override fun onStopWatchTick(time: Long, nanoTime: Long, currentNanoTime: Long) {
        this.currentNanoTime = currentNanoTime
        // We have to check tvTime on null cause it couldn't be ready yet
        tvTime?.text = TimeUtils.format(time)
        // We have to check adapter on null cause it couldn't be ready yet
        timerListController.updateTime(currentNanoTime)
    }


    private fun openSetCarDialog(sessionId: Long, cars: List<Car>) =
        SetCarDialogFragment.newInstance(sessionId, cars).show(childFragmentManager, "set_car")

    private fun openSetDriverDialog(sessionId: Long, drivers: List<Driver>) =
        SetDriverDialogFragment.newInstance(sessionId, drivers).show(childFragmentManager, "set_driver")


    private fun showClearDialog() = AlertDialog.Builder(requireContext())
        .setMessage(R.string.teams_remove_all)
        .setPositiveButton(R.string.yes) { _, _ -> viewModel.removeAll() }
        .setNegativeButton(R.string.no) { _, _ -> }
        .show()
}