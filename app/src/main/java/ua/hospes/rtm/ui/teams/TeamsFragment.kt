package ua.hospes.rtm.ui.teams

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_teams.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsMainFragment
import ua.hospes.rtm.domain.team.Team

@AndroidEntryPoint
class TeamsFragment : AbsMainFragment(R.layout.fragment_teams) {
    private val viewModel: TeamsViewModel by viewModels()
    private val adapter = TeamsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.teams_title


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        adapter.itemClickListener = { showEditDialog(it) }

        viewModel.teams.observe(viewLifecycleOwner) { adapter.submitList(it) }
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