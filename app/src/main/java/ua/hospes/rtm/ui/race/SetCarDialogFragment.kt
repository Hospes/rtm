package ua.hospes.rtm.ui.race

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment

import com.google.common.collect.Collections2

import java.util.ArrayList
import java.util.Collections

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.race.RaceInteractor
import ua.hospes.rtm.utils.RxUtils

/**
 * @author Andrew Khloponin
 */
class SetCarDialogFragment : AppCompatDialogFragment() {
    @Inject
    internal var raceInteractor: RaceInteractor? = null
    private val cars = ArrayList<Car>()
    private var titles = arrayOf<String>()
    private var sessionId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sessionId = arguments!!.getInt(KEY_SESSION_ID, -1)

            val array = arguments!!.getParcelableArray(KEY_CARS)
            Collections.addAll(cars, *if (array == null) arrayOf() else array as Array<Car>)

            titles = Collections2.transform(cars) { input -> input.number.toString() }.toTypedArray()
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
                .setTitle("Select car")
                .setItems(titles) { dialog, which ->
                    raceInteractor!!.setCar(sessionId, cars[which])
                            .compose(RxUtils.applySchedulers())
                            .subscribe({ aBoolean -> }, Consumer<Throwable> { it.printStackTrace() })
                }
                .create()
    }

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