package ua.hospes.rtm.ui.race

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_race_item.*
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.ui.drivers.TeamSpinnerAdapter

@AndroidEntryPoint
class AddTeamToRaceDialogFragment : DialogFragment(R.layout.dialog_add_race_item) {
    private val viewModel: AddTeamToRaceViewModel by viewModels()
    private val adapter by lazy { TeamSpinnerAdapter(requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp_team.adapter = adapter

        btn_save.setOnClickListener { save() }
        btn_cancel.setOnClickListener { dismiss() }

        fetchTeams()
    }

    private fun fetchTeams() = lifecycleScope.launch {
        adapter.clear()
        adapter.addAll(viewModel.getTeams())
    }

    private fun save() = lifecycleScope.launch {
        viewModel.save(number.text.toString(), sp_team.selectedItem as? Team)
        dismiss()
        Toast.makeText(context, "Team added to race!", Toast.LENGTH_SHORT).show()
    }
}