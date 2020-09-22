package ua.hospes.rtm.ui.teams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_edit_team.*
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.extentions.extra

@AndroidEntryPoint
class EditTeamDialogFragment : DialogFragment(R.layout.dialog_edit_team) {
    private val viewModel: EditTeamViewModel by viewModels()
    private val team by extra<Team>(KEY_TEAM)


    companion object {
        private const val KEY_TEAM = "team"
        private const val REQUEST_CODE_SELECT_DRIVERS = 11

        fun newInstance(item: Team?) = EditTeamDialogFragment()
                .apply { arguments = Bundle().apply { putParcelable(KEY_TEAM, item) } }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_assign_drivers.setOnClickListener { showSelectDialog(viewModel.getSelectedDrivers()) }

        btn_save.setOnClickListener { save(et_name.text) }
        btn_cancel.setOnClickListener { dismiss() }
        btn_delete.setOnClickListener { delete() }

        viewModel.team.observe(this) {
            et_name.setText(it?.name)
            btn_delete.isEnabled = it != null
        }
        viewModel.drivers.observe(this) { list ->
            tv_drivers.text = when (list == null || list.isEmpty()) {
                true -> "No drivers"
                false -> list.map { it.name }.toString().replace("[\\[\\]]".toRegex(), "")
            }
        }

        if (savedInstanceState == null) {
            viewModel.initTeam(team)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = when (requestCode) {
        REQUEST_CODE_SELECT_DRIVERS -> when (resultCode) {
            Activity.RESULT_OK -> viewModel.onDriversSelected(data?.getParcelableArrayListExtra("drivers"))
            else -> Unit
        }
        else -> super.onActivityResult(requestCode, resultCode, data)
    }


    private fun showSelectDialog(selected: List<Driver>) =
            SelectDriversDialogFragment.newInstance(selected).apply {
                setTargetFragment(this@EditTeamDialogFragment, REQUEST_CODE_SELECT_DRIVERS)
            }.show(parentFragmentManager, "select_drivers")


    private fun save(name: CharSequence?) = lifecycleScope.launch {
        viewModel.save(et_name.text?.toString() ?: "")
        dismiss()
    }

    private fun delete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }
}