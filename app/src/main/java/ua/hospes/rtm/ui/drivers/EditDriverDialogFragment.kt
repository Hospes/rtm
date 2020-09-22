package ua.hospes.rtm.ui.drivers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_edit_driver.*
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.extentions.extra

@AndroidEntryPoint
class EditDriverDialogFragment : DialogFragment(R.layout.dialog_edit_driver) {
    private val viewModel: EditDriverViewModel by viewModels()
    private val driver by extra<Driver>(KEY_DRIVER)
    private val adapter by lazy { TeamSpinnerAdapter(requireContext()) }


    companion object {
        private const val KEY_DRIVER = "driver"

        fun newInstance(driver: Driver?) = EditDriverDialogFragment()
                .apply { arguments = Bundle().apply { putParcelable(KEY_DRIVER, driver) } }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp_team.prompt = "Select team"
        sp_team.adapter = adapter

        btn_save.setOnClickListener { clickSave() }
        btn_cancel.setOnClickListener { dismiss() }
        btn_delete.setOnClickListener { clickDelete() }

        viewModel.driver.observe(this) { onDriver(it) }
        viewModel.teams.observe(this) { onTeams(it) }

        if (savedInstanceState == null) {
            viewModel.initDriver(driver)
        }
    }


    private fun onDriver(driver: Driver?) {
        btn_delete.isEnabled = driver != null
        driver ?: return
        et_name.setText(driver.name)
        et_name.setSelection(driver.name.length)
    }

    private fun onTeams(teams: List<Team>) {
        adapter.clear()
        adapter.add(null)
        adapter.addAll(teams)

        preselectTeam()
    }

    private fun preselectTeam() = lifecycleScope.launch {
        val i = viewModel.getDriverTeamIndex()
        if (i != -1) sp_team.setSelection(i + 1)
    }

    private fun clickSave() = lifecycleScope.launch {
        viewModel.save(et_name.text.toString(), sp_team.selectedItem as? Team)
        dismiss()
    }.let { Unit }

    private fun clickDelete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }.let { Unit }
}