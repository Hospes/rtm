package ua.hospes.rtm.ui.drivers

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsMainFragment
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.theme.RTMTheme

@AndroidEntryPoint
class DriversFragment : AbsMainFragment() {
    private val viewModel: DriversViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.drivers_title


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        // Dispose the Composition when viewLifecycleOwner is destroyed
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))

        setContent {
            RTMTheme {
                Drivers(openEditDriver = { showEditDialog(it) })
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.drivers, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> showEditDialog(null).let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun showEditDialog(item: Driver?) =
        EditDriverDialogFragment.newInstance(item).show(childFragmentManager, "add_driver")

    private fun showClearDialog() = AlertDialog.Builder(requireContext())
        .setMessage(R.string.drivers_remove_all)
        .setPositiveButton(R.string.yes) { _, _ -> viewModel.removeAll() }
        .setNegativeButton(R.string.no) { _, _ -> }
        .show()
}