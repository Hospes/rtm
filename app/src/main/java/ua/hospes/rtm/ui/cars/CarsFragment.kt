package ua.hospes.rtm.ui.cars

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsMainFragment
import ua.hospes.rtm.databinding.FragmentCarsBinding
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.utils.ViewBindingHolder

@AndroidEntryPoint
class CarsFragment : AbsMainFragment(R.layout.fragment_cars), ViewBindingHolder<FragmentCarsBinding> by ViewBindingHolder.Impl() {
    private val viewModel: CarsViewModel by viewModels()
    private val adapter = CarsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.cars_title


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(FragmentCarsBinding.bind(view), this)

        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.cars_column_count))
        binding.list.adapter = adapter
        adapter.itemClickListener = { showEditCarDialog(it) }

        viewModel.cars.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.cars, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> showEditCarDialog(null).let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun showEditCarDialog(car: Car?) = EditCarDialogFragment.newInstance(car).show(childFragmentManager, "add_car")

    private fun showClearDialog() = AlertDialog.Builder(requireContext())
            .setMessage(R.string.cars_remove_all)
            .setPositiveButton(R.string.yes) { _, _ -> viewModel.removeAll() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
}