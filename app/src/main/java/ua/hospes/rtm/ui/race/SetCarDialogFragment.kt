package ua.hospes.rtm.ui.race

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.SessionsRepository
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.utils.extentions.extraNotNull
import javax.inject.Inject

@AndroidEntryPoint
class SetCarDialogFragment : DialogFragment() {
    @Inject lateinit var sesRepo: SessionsRepository
    private val sessionId by extraNotNull<Long>(KEY_SESSION_ID)
    private val cars by extraNotNull<List<Car>>(KEY_CARS)

    companion object {
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_CARS = "cars"

        fun newInstance(sessionId: Long, cars: List<Car>): SetCarDialogFragment = SetCarDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_SESSION_ID, sessionId)
                putParcelableArrayList(KEY_CARS, ArrayList(cars))
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select car")
            .setItems(cars.map { it.number.toString() }.toTypedArray()) { _, which -> setCar(sessionId, cars[which].id) }
            .create()

    private fun setCar(sessionId: Long, carId: Long) = lifecycleScope.launch { sesRepo.setSessionCar(sessionId, carId) }
}