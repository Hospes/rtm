package ua.hospes.rtm.ui.race

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import dagger.android.support.AndroidSupportInjection
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.race.RaceInteractor
import java.util.*
import javax.inject.Inject

internal class SetCarDialogFragment : AppCompatDialogFragment() {
    @Inject lateinit var raceInteractor: RaceInteractor
    private val cars = ArrayList<Car>()
    private var titles = arrayOf<String>()
    private var sessionId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sessionId = arguments!!.getInt(KEY_SESSION_ID, -1)

            val array = arguments!!.getParcelableArray(KEY_CARS)
            Collections.addAll(cars, *if (array == null) arrayOf() else array as Array<Car>)

            titles = cars.map { it.number.toString() }.toTypedArray()
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(activity!!)
            .setTitle("Select car")
            .setItems(titles) { _, which ->
                //                raceInteractor.setCar(sessionId, cars[which])
                //                        .compose(RxUtils.applySchedulers())
                //                        .subscribe({ }, Throwable::printStackTrace)
            }
            .create()

    companion object {
        private val KEY_SESSION_ID = "session_id"
        private val KEY_CARS = "cars"


        fun newInstance(sessionId: Int, cars: List<Car>): SetCarDialogFragment {
            val dialog = SetCarDialogFragment()

            val bundle = Bundle()
            bundle.putInt(KEY_SESSION_ID, sessionId)
            bundle.putParcelableArray(KEY_CARS, cars.toTypedArray())
            dialog.arguments = bundle

            return dialog
        }
    }
}