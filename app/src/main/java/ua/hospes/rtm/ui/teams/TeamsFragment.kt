package ua.hospes.rtm.ui.teams

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_teams.*
import timber.log.Timber
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsFragment
import ua.hospes.rtm.domain.team.Team
import javax.inject.Inject

@AndroidEntryPoint
class TeamsFragment : AbsFragment(R.layout.fragment_teams), TeamsContract.View {
    @Inject lateinit var presenter: TeamsPresenter
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

        presenter.attachView(this, lifecycle)
    }


    //region ActionBar Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.teams, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> showEditDialog(null).let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }
    //endregion


    override fun onData(list: List<Team>) = adapter.submitList(list).also { Timber.d("Teams update: $list") }

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()


    private fun showEditDialog(item: Team?) = EditTeamDialogFragment.newInstance(item).show(childFragmentManager, "add_team")

    private fun showClearDialog() = AlertDialog.Builder(requireContext())
            .setMessage(R.string.teams_remove_all)
            .setPositiveButton(R.string.yes) { _, _ -> presenter.removeAll() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
}