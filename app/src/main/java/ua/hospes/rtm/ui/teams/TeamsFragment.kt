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
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsMainFragment
import ua.hospes.rtm.databinding.FragmentTeamsBinding
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.ViewBindingHolder

@AndroidEntryPoint
class TeamsFragment : AbsMainFragment(R.layout.fragment_teams), ViewBindingHolder<FragmentTeamsBinding> by ViewBindingHolder.Impl() {
    private val viewModel: TeamsViewModel by viewModels()
    private val adapter = TeamsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setActionBarTitle(): Int = R.string.teams_title


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(FragmentTeamsBinding.bind(view), this)

        binding.list.setHasFixedSize(true)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
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