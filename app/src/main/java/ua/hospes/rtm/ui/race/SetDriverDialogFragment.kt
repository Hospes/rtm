package ua.hospes.rtm.ui.race

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import dagger.android.support.AndroidSupportInjection
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.race.RaceInteractor
import ua.hospes.rtm.utils.RxUtils
import java.util.*
import javax.inject.Inject

class SetDriverDialogFragment : AppCompatDialogFragment() {
    @Inject lateinit var raceInteractor: RaceInteractor
    private val drivers = ArrayList<Driver>()
    private var titles = arrayOf<String>()
    private var sessionId = -1
    private var teamId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sessionId = arguments!!.getInt(KEY_SESSION_ID, -1)
            teamId = arguments!!.getInt(KEY_TEAM_ID, -1)

            val array = arguments!!.getParcelableArray(KEY_DRIVERS)
            Collections.addAll(drivers, *if (array == null) arrayOf() else array as Array<Driver>)

            titles = drivers.map { it.name }.toTypedArray()
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(activity!!)
            .setTitle("Select driver")
            .setItems(titles) { _, which ->
                val (id) = drivers[which]
                raceInteractor.setDriver(sessionId, teamId, id!!)
                        .compose(RxUtils.applySchedulers())
                        .subscribe({ }, Throwable::printStackTrace)
            }
            .create()

    companion object {
        private val KEY_SESSION_ID = "session_id"
        private val KEY_TEAM_ID = "team_id"
        private val KEY_DRIVERS = "drivers"


        fun newInstance(sessionId: Int, teamId: Int, drivers: List<Driver>): SetDriverDialogFragment {
            val dialog = SetDriverDialogFragment()

            val bundle = Bundle()
            bundle.putInt(KEY_SESSION_ID, sessionId)
            bundle.putInt(KEY_TEAM_ID, teamId)
            bundle.putParcelableArray(KEY_DRIVERS, drivers.toTypedArray())
            dialog.arguments = bundle

            return dialog
        }
    }
}