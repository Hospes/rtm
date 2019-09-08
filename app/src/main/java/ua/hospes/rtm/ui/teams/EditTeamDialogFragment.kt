package ua.hospes.rtm.ui.teams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_edit_team.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.DiDialogFragment
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.extentions.extra
import javax.inject.Inject

private const val KEY_TEAM = "team"
private const val REQUEST_CODE_SELECT_DRIVERS = 11

internal class EditTeamDialogFragment : DiDialogFragment(), EditTeamContract.View {
    @Inject lateinit var presenter: EditTeamPresenter
    private val team by extra<Team>(KEY_TEAM)


    companion object {
        @JvmStatic fun newInstance(item: Team?) = EditTeamDialogFragment()
                .apply { arguments = Bundle().apply { putParcelable(KEY_TEAM, item) } }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.dialog_edit_team, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_assign_drivers.setOnClickListener { presenter.clickSelectDrivers() }

        btn_save.setOnClickListener { presenter.save(et_name.text) }
        btn_cancel.setOnClickListener { dismiss() }
        btn_delete.setOnClickListener { presenter.delete() }

        presenter.initTeam(team)
        presenter.attachView(this, lifecycle)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = when (requestCode) {
        REQUEST_CODE_SELECT_DRIVERS -> when (resultCode) {
            Activity.RESULT_OK -> presenter.onDriversSelected(data?.getParcelableArrayListExtra("drivers"))
            else -> Unit
        }
        else -> super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onInitTeam(team: Team) {
        et_name.setText(team.name)
    }

    override fun onSelectedDrivers(list: List<Driver>) {
        tv_drivers.text = when (list.isEmpty()) {
            true -> "No drivers"
            false -> list.map { it.name }.toString().replace("[\\[\\]]".toRegex(), "")
        }
    }

    override fun onShowSelectDialog(selected: List<Driver>) =
            SelectDriversDialogFragment.newInstance(selected).apply {
                setTargetFragment(this@EditTeamDialogFragment, REQUEST_CODE_SELECT_DRIVERS)
            }.show(requireFragmentManager(), "select_drivers")

    override fun onDeleteButtonAvailable(available: Boolean) = with(btn_delete) { isEnabled = available }

    override fun onSaved() = dismiss()
    override fun onDeleted() = dismiss()

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
}