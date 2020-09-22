package ua.hospes.rtm.ui.teams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_select_drivers.*
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.utils.extentions.extraNotNull

@AndroidEntryPoint
class SelectDriversDialogFragment : DialogFragment(R.layout.dialog_select_drivers) {
    private val viewModel: SelectDriversViewModel by viewModels()
    private val adapter = SelectDriversAdapter()
    private val selected by extraNotNull(KEY_DRIVERS, emptyList<Driver>())


    companion object {
        private const val KEY_DRIVERS = "drivers"

        fun newInstance(drivers: List<Driver>) = SelectDriversDialogFragment()
                .apply { arguments = Bundle().apply { putParcelableArrayList(KEY_DRIVERS, ArrayList(drivers)) } }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        adapter.setSelected(selected)

        btn_cancel.setOnClickListener { dismiss() }
        btn_save.setOnClickListener { save(adapter.getSelectedIds()) }

        viewModel.drivers.observe(this) { adapter.submitList(it) }
    }


    private fun save(selectedIds: List<Long>) = lifecycleScope.launch { onSaveSelectedDrivers(viewModel.save(selectedIds)) }

    private fun onSaveSelectedDrivers(list: List<Driver>) {
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().putExtra("drivers", ArrayList(list)))
        dismiss()
    }
}