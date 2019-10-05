package ua.hospes.rtm.ui.race

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_race.*
import timber.log.Timber
import ua.hospes.rtm.R
import ua.hospes.rtm.core.StopWatchFragment
import ua.hospes.rtm.core.StopWatchService
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.utils.TimeUtils
import ua.hospes.undobutton.UndoButtonController
import javax.inject.Inject

private const val REQUEST_CODE_PERMISSION = 11

internal class RaceFragment : StopWatchFragment(R.layout.fragment_race), RaceContract.View {
    private lateinit var undoController: UndoButtonController<*>
    private lateinit var timerListController: TimerListController
    @Inject lateinit var presenter: RacePresenter
    @Inject lateinit var preferencesManager: PreferencesManager
    private var tvTime: TextView? = null
    private var currentNanoTime = 0L

    private val sessionButtonType: String by lazy { preferencesManager.sessionButtonType }
    private val adapter: RaceAdapter by lazy { RaceAdapter(requireContext(), sessionButtonType, undoController) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.race_title

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        undoController = object : UndoButtonController<RaceAdapter.MyHolderUndoNext>(requireContext()) {
            override fun provideUndos(holder: RaceAdapter.MyHolderUndoNext) = arrayOf(holder.btnNextSession)

            override fun defaultTimeSHow(): Boolean = true

            override fun defaultDelay(): Int = 15
        }

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        list.itemAnimator = null

        adapter.onPitClickListener = { presenter.onPit(it, currentNanoTime) }
        adapter.onOutClickListener = { presenter.onOut(it, currentNanoTime) }
        adapter.onUndoClickListener = { presenter.undoLastSession(it) }
        adapter.setCarClickListener = { presenter.clickSetCar(it) }
        adapter.setDriverClickListener = { presenter.clickSetDriver(it) }
        adapter.itemClickListener = { /*RaceItemDetailActivity.start(context, item.id)*/ }

        timerListController = TimerListController(presenter).apply { list.addOnScrollListener(this) }

        if ("undo".equals(sessionButtonType, ignoreCase = true))
            list.addOnScrollListener(undoController)

        presenter.attachView(this, lifecycle)
    }


    //region ActionBar Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.race, menu)
    }

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
            timerListController.forceUpdate(list)
            true
        }

        R.id.action_stop -> StopWatchService.stop(requireContext()).let { true }
        R.id.action_add_team -> AddTeamToRaceDialogFragment().show(childFragmentManager, "add_team_to_race").let { true }

        R.id.action_export -> {
            // Assume thisActivity is the current activity
            val permissionCheck = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED)
                presenter.exportXLS()
            else
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
            true
        }

        R.id.action_reset -> presenter.resetRace().let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }
    //endregion

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) = when (requestCode) {
        REQUEST_CODE_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.exportXLS()
        } else Unit

        else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        undoController.release()
    }

    override fun onData(items: List<RaceItem>) {
        Timber.d(items.toString())
        adapter.submitList(items)
        timerListController.forceUpdate(list)
        if ("undo".equals(sessionButtonType, ignoreCase = true))
            undoController.forceUpdate(list)
    }

    override fun onError(t: Throwable) = Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()


    override fun onStopWatchStarted(startTime: Long, nanoStartTime: Long) = presenter.startRace(nanoStartTime).let { Unit }
    override fun onStopWatchStopped(stopTime: Long, nanoStopTime: Long) = presenter.stopRace(nanoStopTime).let { Unit }
    override fun onStopWatchStateChanged(runningState: Int) = activity?.invalidateOptionsMenu() ?: Unit

    override fun onStopWatchTick(time: Long, nanoTime: Long, currentNanoTime: Long) {
        this.currentNanoTime = currentNanoTime
        // We have to check tvTime on null cause it couldn't be ready yet
        tvTime?.text = TimeUtils.format(time)
        // We have to check adapter on null cause it couldn't be ready yet
        timerListController.updateTime(currentNanoTime)
    }


    override fun onOpenSetCarDialog(sessionId: Long, cars: List<Car>) =
            SetCarDialogFragment.newInstance(sessionId, cars).show(childFragmentManager, "set_car")

    override fun onOpenSetDriverDialog(sessionId: Long, drivers: List<Driver>) =
            SetDriverDialogFragment.newInstance(sessionId, drivers).show(childFragmentManager, "set_driver")


    private fun showClearDialog() = AlertDialog.Builder(requireContext())
            .setMessage(R.string.teams_remove_all)
            .setPositiveButton(R.string.yes) { _, _ -> presenter.removeAll() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
}