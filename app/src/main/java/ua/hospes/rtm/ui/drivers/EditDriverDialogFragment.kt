package ua.hospes.rtm.ui.drivers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.DialogEditDriverBinding
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.extentions.extra

@AndroidEntryPoint
class EditDriverDialogFragment : DialogFragment(R.layout.dialog_edit_driver) {
    private val viewModel: EditDriverViewModel by viewModels()
    private val binding by viewBinding(DialogEditDriverBinding::bind)
    private val driver by extra<Driver>(KEY_DRIVER)


    companion object {
        private const val KEY_DRIVER = "driver"

        fun newInstance(driver: Driver?) = EditDriverDialogFragment()
            .apply { arguments = Bundle().apply { putParcelable(KEY_DRIVER, driver) } }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TeamSpinnerAdapter(requireContext())

        binding.spTeam.prompt = "Select team"
        binding.spTeam.adapter = adapter

        binding.btnSave.setOnClickListener { clickSave() }
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnDelete.setOnClickListener { clickDelete() }

        viewModel.driver.observe(this) { onDriver(it) }
        viewModel.teams.observe(this) { onTeams(adapter, it) }

        if (savedInstanceState == null) {
            viewModel.initDriver(driver)
        }
    }


    private fun onDriver(driver: Driver?) {
        binding.btnDelete.isEnabled = driver != null
        driver ?: return
        binding.etName.setText(driver.name)
        binding.etName.setSelection(driver.name.length)
    }

    private fun onTeams(adapter: TeamSpinnerAdapter, teams: List<Team>) {
        adapter.clear()
        adapter.add(null)
        adapter.addAll(teams)

        preselectTeam()
    }

    private fun preselectTeam() = lifecycleScope.launch {
        val i = viewModel.getDriverTeamIndex()
        if (i != -1) binding.spTeam.setSelection(i + 1)
    }

    private fun clickSave() = lifecycleScope.launch {
        viewModel.save(binding.etName.text.toString(), binding.spTeam.selectedItem as? Team)
        dismiss()
    }.let { Unit }

    private fun clickDelete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }.let { Unit }
}