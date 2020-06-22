package ua.hospes.rtm.ui.drivers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_drivers.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsMainFragment
import ua.hospes.rtm.domain.drivers.Driver
import javax.inject.Inject

@AndroidEntryPoint
class DriversFragment : AbsMainFragment(R.layout.fragment_drivers), DriversContract.View {
    @Inject lateinit var presenter: DriversPresenter
    private val adapter = DriversAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.drivers_title

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        adapter.itemClickListener = { showEditDriverDialog(it) }

        presenter.attachView(this, lifecycle)
    }


    //region ActionBar Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.drivers, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> showEditDriverDialog(null).let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }
    //endregion


    override fun onData(list: List<Driver>) = adapter.submitList(list)

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()


    private fun showEditDriverDialog(driver: Driver?) =
            EditDriverDialogFragment.newInstance(driver).show(childFragmentManager, "add_driver")

    private fun showClearDialog() = AlertDialog.Builder(requireContext())
            .setMessage(R.string.drivers_remove_all)
            .setPositiveButton(R.string.yes) { _, _ -> presenter.removeAll() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
}