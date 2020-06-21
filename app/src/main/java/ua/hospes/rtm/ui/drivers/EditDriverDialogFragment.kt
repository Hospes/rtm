package ua.hospes.rtm.ui.drivers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_edit_driver.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.DiDialogFragment
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.extentions.extra
import javax.inject.Inject

private const val KEY_DRIVER = "driver"

internal class EditDriverDialogFragment : DiDialogFragment(), EditDriverContract.View {
    @Inject lateinit var presenter: EditDriverPresenter
    private val driver by extra<Driver>(KEY_DRIVER)
    private lateinit var adapter: TeamSpinnerAdapter


    companion object {
        @JvmStatic fun newInstance(driver: Driver?) = EditDriverDialogFragment()
                .apply { arguments = Bundle().apply { putParcelable(KEY_DRIVER, driver) } }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.dialog_edit_driver, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TeamSpinnerAdapter(context!!)
        sp_team.prompt = "Select team"
        sp_team.adapter = adapter


        btn_save.setOnClickListener { presenter.save(et_name.text, sp_team.selectedItem as? Team) }
        btn_cancel.setOnClickListener { dismiss() }
        btn_delete.setOnClickListener { presenter.delete() }

        presenter.initDriver(driver)
        presenter.attachView(this, lifecycle)
    }


    override fun onInitDriver(driver: Driver) {
        et_name.setText(driver.name)
        et_name.setSelection(driver.name.length)
    }

    override fun onTeamSelectionIndex(index: Int) {
        sp_team.setSelection(index + 1)
    }

    override fun onTeamsLoaded(teams: List<Team>) {
        adapter.clear()
        adapter.add(null)
        adapter.addAll(teams)
    }

    override fun onDeleteButtonAvailable(available: Boolean) = with(btn_delete) { isEnabled = available }

    override fun onSaved() = dismiss()
    override fun onDeleted() = dismiss()

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
}