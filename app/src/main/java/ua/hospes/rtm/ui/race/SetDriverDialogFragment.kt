package ua.hospes.rtm.ui.race

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ua.hospes.rtm.core.DiDialogFragment
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.utils.extentions.extraNotNull
import java.util.*
import javax.inject.Inject

private const val KEY_SESSION_ID = "session_id"
private const val KEY_DRIVERS = "drivers"

internal class SetDriverDialogFragment : DiDialogFragment(), SetDriverContract.View {
    @Inject lateinit var presenter: SetDriverPresenter
    private val sessionId by extraNotNull<Long>(KEY_SESSION_ID)
    private val drivers by extraNotNull<List<Driver>>(KEY_DRIVERS)

    companion object {
        fun newInstance(sessionId: Long, drivers: List<Driver>): SetDriverDialogFragment = SetDriverDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_SESSION_ID, sessionId)
                putParcelableArrayList(KEY_DRIVERS, ArrayList(drivers))
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this, lifecycle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select driver")
            .setItems(drivers.map { it.name }.toTypedArray()) { _, which ->
                presenter.setDriver(sessionId, drivers[which].id)
            }
            .create()

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
}