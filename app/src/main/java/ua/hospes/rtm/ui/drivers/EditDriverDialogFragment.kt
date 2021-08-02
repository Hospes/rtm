package ua.hospes.rtm.ui.drivers

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.DialogEditDriverBinding
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team

@AndroidEntryPoint
class EditDriverDialogFragment : DialogFragment(R.layout.dialog_edit_driver) {
    private val viewModel: EditDriverViewModel by viewModels()


    companion object {
        fun newInstance(driver: Driver?) = EditDriverDialogFragment().apply { arguments = bundleOf("driver" to driver) }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogEditDriverBinding.bind(view)
        val adapter = TeamSpinnerAdapter(requireContext())

        binding.spTeam.prompt = "Select team"
        binding.spTeam.adapter = adapter

        binding.btnSave.setOnClickListener { clickSave(binding) }
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnDelete.setOnClickListener { clickDelete() }

        viewModel.driver.observe(this) { onDriver(binding, it) }
        viewModel.teams.observe(this) { onTeams(binding.spTeam, adapter, it) }
    }


    private fun onDriver(binding: DialogEditDriverBinding, driver: Driver?) {
        binding.btnDelete.isEnabled = driver != null
        driver ?: return
        binding.etName.setText(driver.name)
        binding.etName.setSelection(driver.name.length)
    }

    private fun onTeams(spinner: Spinner, adapter: TeamSpinnerAdapter, teams: List<Team>) {
        adapter.clear()
        adapter.add(null)
        adapter.addAll(teams)

        preselectTeam(spinner)
    }

    private fun preselectTeam(spinner: Spinner) = lifecycleScope.launch {
        val i = viewModel.getDriverTeamIndex()
        if (i != -1) spinner.setSelection(i + 1)
    }

    private fun clickSave(binding: DialogEditDriverBinding) = lifecycleScope.launch {
        viewModel.save(binding.etName.text.toString(), binding.spTeam.selectedItem as? Team)
        dismiss()
    }.let { }

    private fun clickDelete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }.let { }
}