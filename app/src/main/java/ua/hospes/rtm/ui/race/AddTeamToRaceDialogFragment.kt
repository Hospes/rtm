package ua.hospes.rtm.ui.race

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_add_race_item.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.DiDialogFragment
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.ui.drivers.TeamSpinnerAdapter
import javax.inject.Inject

internal class AddTeamToRaceDialogFragment : DiDialogFragment(), AddTeamToRaceContract.View {
    @Inject lateinit var presenter: AddTeamToRacePresenter
    private lateinit var adapter: TeamSpinnerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.dialog_add_race_item, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TeamSpinnerAdapter(requireContext())
        sp_team.adapter = adapter

        btn_save.setOnClickListener { presenter.save(number.text.toString(), sp_team.selectedItem as? Team) }
        btn_cancel.setOnClickListener { dismiss() }

        presenter.attachView(this, lifecycle)
    }

    override fun onTeams(list: List<Team>) = with(adapter) {
        clear()
        addAll(list)
    }

    override fun onSuccess() {
        dismiss()
        Toast.makeText(context, "Team added to race!", Toast.LENGTH_SHORT).show()
    }

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
}