package ua.hospes.rtm.ui.race

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.utils.extentions.extraNotNull
import javax.inject.Inject

private const val KEY_SESSION_ID = "session_id"
private const val KEY_CARS = "cars"

@AndroidEntryPoint
class SetCarDialogFragment : DialogFragment(), SetCarContract.View {
    @Inject lateinit var presenter: SetCarPresenter
    private val sessionId by extraNotNull<Long>(KEY_SESSION_ID)
    private val cars by extraNotNull<List<Car>>(KEY_CARS)

    companion object {
        fun newInstance(sessionId: Long, cars: List<Car>): SetCarDialogFragment = SetCarDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_SESSION_ID, sessionId)
                putParcelableArrayList(KEY_CARS, ArrayList(cars))
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this, lifecycle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select car")
            .setItems(cars.map { it.number.toString() }.toTypedArray()) { _, which ->
                presenter.setCar(sessionId, cars[which].id)
            }
            .create()

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
}