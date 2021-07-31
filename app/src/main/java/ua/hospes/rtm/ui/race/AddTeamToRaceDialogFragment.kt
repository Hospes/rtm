package ua.hospes.rtm.ui.race

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.DialogAddRaceItemBinding
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.ui.drivers.TeamSpinnerAdapter

@AndroidEntryPoint
class AddTeamToRaceDialogFragment : DialogFragment(R.layout.dialog_add_race_item) {
    private val viewModel: AddTeamToRaceViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogAddRaceItemBinding.bind(view)
        val adapter = TeamSpinnerAdapter(requireContext())
        binding.spTeam.adapter = adapter

        binding.btnSave.setOnClickListener { save(binding) }
        binding.btnCancel.setOnClickListener { dismiss() }

        fetchTeams(adapter)
    }

    private fun fetchTeams(adapter: TeamSpinnerAdapter) = lifecycleScope.launch {
        adapter.clear()
        adapter.addAll(viewModel.getTeams())
    }

    private fun save(binding: DialogAddRaceItemBinding) = lifecycleScope.launch {
        viewModel.save(binding.number.text.toString(), binding.spTeam.selectedItem as? Team)
        dismiss()
        Toast.makeText(context, "Team added to race!", Toast.LENGTH_SHORT).show()
    }
}