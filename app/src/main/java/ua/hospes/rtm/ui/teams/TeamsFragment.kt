package ua.hospes.rtm.ui.teams

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsMainFragment
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.theme.RTMTheme

@AndroidEntryPoint
class TeamsFragment : AbsMainFragment() {
    private val viewModel: TeamsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.teams_title


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        // Dispose the Composition when viewLifecycleOwner is destroyed
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))

        setContent {
            RTMTheme {
                Teams(openEditTeam = { showEditDialog(it) })
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.teams, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> showEditDialog(null).let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun showEditDialog(item: Team?) = EditTeamDialogFragment.newInstance(item).show(childFragmentManager, "add_team")

    private fun showClearDialog() = AlertDialog.Builder(requireContext())
        .setMessage(R.string.teams_remove_all)
        .setPositiveButton(R.string.yes) { _, _ -> viewModel.removeAll() }
        .setNegativeButton(R.string.no) { _, _ -> }
        .show()
}