package ua.hospes.rtm.ui.race

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import ua.hospes.rtm.R
import ua.hospes.rtm.core.StopWatchFragment
import ua.hospes.rtm.core.StopWatchService
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.utils.TimeUtils
import ua.hospes.rtm.utils.UiUtils
import ua.hospes.undobutton.UndoButton
import ua.hospes.undobutton.UndoButtonController
import javax.inject.Inject

class RaceFragment : StopWatchFragment(), RaceContract.View {
    private var undoController: UndoButtonController<*>? = null
    private var timerListController: TimerListController? = null
    @Inject
    internal var presenter: RacePresenter? = null
    @Inject
    internal var preferencesManager: PreferencesManager? = null
    private var tvTime: TextView? = null
    private var rv: RecyclerView? = null
    private var adapter: RaceAdapter? = null
    private var currentNanoTime = 0L

    private var sessionButtonType: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        sessionButtonType = preferencesManager!!.sessionButtonType
    }

    override fun setActionBarTitle(): Int {
        return R.string.race_title
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_race, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = UiUtils.findView<RecyclerView>(view, R.id.list)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        undoController = object : UndoButtonController<RaceAdapter.MyHolderUndoNext>(context!!) {
            override fun provideUndos(holder: RaceAdapter.MyHolderUndoNext): Array<UndoButton> {
                return arrayOf(holder.btnNextSession)
            }

            override fun defaultTimeSHow(): Boolean {
                return true
            }

            override fun defaultDelay(): Int {
                return 15
            }
        }

        rv!!.setHasFixedSize(true)
        rv!!.layoutManager = LinearLayoutManager(context)
        rv!!.adapter = RaceAdapter(context, sessionButtonType, undoController)

        adapter!!.setOnPitClickListener { item, position -> presenter!!.onPit(item, currentNanoTime) }
        adapter!!.setOnOutClickListener { item, position -> presenter!!.onOut(item, currentNanoTime) }
        adapter!!.setOnUndoClickListener { item, position -> presenter!!.undoLastSession(item) }
        adapter!!.setOnSetCarClickListener { item, position -> presenter!!.showSetCarDialog(childFragmentManager, item.session) }
        adapter!!.setOnSetDriverClickListener { item, position -> presenter!!.showSetDriverDialog(childFragmentManager, item.session) }
        adapter!!.setOnItemClickListener { item, position -> presenter!!.showRaceItemDetail(context, item) }

        rv!!.addOnScrollListener(timerListController = TimerListController())
        if ("undo".equals(sessionButtonType!!, ignoreCase = true))
            rv!!.addOnScrollListener(undoController!!)

        presenter!!.attachView(this)
    }


    //region ActionBar Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.race, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val stopWatchStarted = isStopWatchStarted
        menu.findItem(R.id.action_start).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_add_team).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_export).isVisible = preferencesManager!!.isExportXLSEnabled && !stopWatchStarted
        menu.findItem(R.id.action_reset).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_clear).isVisible = !stopWatchStarted
        menu.findItem(R.id.action_stop).isVisible = stopWatchStarted

        val stopWatchItem = menu.findItem(R.id.action_stopwatch)
        if (stopWatchItem != null) {
            tvTime = stopWatchItem.actionView.findViewById(R.id.text)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_start -> {
                StopWatchService.start(context!!)
                timerListController!!.forceUpdate(rv)
                return true
            }

            R.id.action_stop -> {
                StopWatchService.stop(context!!)
                return true
            }

            R.id.action_add_team -> {
                presenter!!.showAddTeamDialog(childFragmentManager)
                return true
            }

            R.id.action_export -> {
                // Assume thisActivity is the current activity
                val permissionCheck = ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (permissionCheck == PackageManager.PERMISSION_GRANTED)
                    presenter!!.exportXLS()
                else
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
                return true
            }

            R.id.action_reset -> {
                presenter!!.resetRace()
                return true
            }

            R.id.action_clear -> {
                showClearDialog()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
    //endregion

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter!!.exportXLS()
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerListController!!.unsubscribe()
        undoController!!.release()
        presenter!!.detachView()
    }

    fun update(items: List<RaceItem>) {
        adapter!!.clear()
        adapter!!.addAll(items)
        timerListController!!.forceUpdate(rv)
        if ("undo".equals(sessionButtonType!!, ignoreCase = true))
            undoController!!.forceUpdate(rv!!)
    }


    override fun onStopWatchStarted(startTime: Long, nanoStartTime: Long) {
        presenter!!.startRace(nanoStartTime)
    }

    override fun onStopWatchStopped(stopTime: Long, nanoStopTime: Long) {
        presenter!!.stopRace(nanoStopTime)
    }

    override fun onStopWatchStateChanged(runningState: Int) {
        activity!!.invalidateOptionsMenu()
    }

    override fun onStopWatchTick(time: Long, nanoTime: Long, currentNanoTime: Long) {
        this.currentNanoTime = currentNanoTime
        // We have to check tvTime on null cause it couldn't be ready yet
        if (tvTime != null) tvTime!!.text = TimeUtils.format(time)
        // We have to check adapter on null cause it couldn't be ready yet
        if (timerListController != null) timerListController!!.updateTime(currentNanoTime)
    }


    private fun showClearDialog() {
        AlertDialog.Builder(context!!)
                .setMessage(R.string.teams_remove_all)
                .setPositiveButton(R.string.yes) { dialog, which -> presenter!!.removeAll() }
                .setNegativeButton(R.string.no) { dialog, which -> }
                .show()
    }

    companion object {
        private val REQUEST_CODE_PERMISSION = 11


        fun newInstance(): Fragment {
            return RaceFragment()
        }
    }
}