package ua.hospes.rtm.ui.teams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.DialogEditTeamBinding
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.ViewBindingHolder
import ua.hospes.rtm.utils.extentions.extra

@AndroidEntryPoint
class EditTeamDialogFragment : DialogFragment(R.layout.dialog_edit_team), ViewBindingHolder<DialogEditTeamBinding> by ViewBindingHolder.Impl() {
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
        initBinding(DialogEditTeamBinding.bind(view), this)

        binding.btnAssignDrivers.setOnClickListener { showSelectDialog(viewModel.getSelectedDrivers()) }

        binding.btnSave.setOnClickListener { save(binding.etName.text) }
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnDelete.setOnClickListener { delete() }

        viewModel.team.observe(this) {
            binding.etName.setText(it?.name)
            binding.btnDelete.isEnabled = it != null
        }
        viewModel.drivers.observe(this) { list ->
            binding.tvDrivers.text = when (list == null || list.isEmpty()) {
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
        viewModel.save(binding.etName.text?.toString() ?: "")
        dismiss()
    }

    private fun delete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }
}