package ua.hospes.rtm.ui.race

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.SessionsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.utils.extentions.extraNotNull
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SetDriverDialogFragment : DialogFragment() {
    @Inject lateinit var sesRepo: SessionsRepository
    private val sessionId by extraNotNull<Long>(KEY_SESSION_ID)
    private val drivers by extraNotNull<List<Driver>>(KEY_DRIVERS)

    companion object {
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_DRIVERS = "drivers"

        fun newInstance(sessionId: Long, drivers: List<Driver>): SetDriverDialogFragment = SetDriverDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_SESSION_ID, sessionId)
                putParcelableArrayList(KEY_DRIVERS, ArrayList(drivers))
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select driver")
            .setItems(drivers.map { it.name }.toTypedArray()) { _, which -> setDriver(sessionId, drivers[which].id) }
            .create()

    private fun setDriver(sessionId: Long, driverId: Long) = lifecycleScope.launch { sesRepo.setSessionDriver(sessionId, driverId) }
}