package ua.hospes.rtm.ui.drivers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsMainFragment
import ua.hospes.rtm.databinding.FragmentDriversBinding
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.utils.ViewBindingHolder

@AndroidEntryPoint
class DriversFragment : AbsMainFragment(R.layout.fragment_drivers), ViewBindingHolder<FragmentDriversBinding> by ViewBindingHolder.Impl() {
    private val viewModel: DriversViewModel by viewModels()
    private val adapter = DriversAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.drivers_title


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(FragmentDriversBinding.bind(view), this)

        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        adapter.itemClickListener = { showEditDriverDialog(it) }

        viewModel.drivers.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.drivers, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> showEditDriverDialog(null).let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun showEditDriverDialog(driver: Driver?) =
            EditDriverDialogFragment.newInstance(driver).show(childFragmentManager, "add_driver")

    private fun showClearDialog() = AlertDialog.Builder(requireContext())
            .setMessage(R.string.drivers_remove_all)
            .setPositiveButton(R.string.yes) { _, _ -> viewModel.removeAll() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
}