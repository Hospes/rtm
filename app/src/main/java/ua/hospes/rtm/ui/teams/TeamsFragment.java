package ua.hospes.rtm.ui.teams;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.team.models.Team;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class TeamsFragment extends Fragment implements TeamsContract.View {
    @Inject TeamsPresenter presenter;

    private RecyclerView rv;
    private TeamAdapter adapter;


    public static Fragment newInstance() {
        return new TeamsFragment();
    }

    public TeamsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teams, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = UiUtils.findView(view, R.id.list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter = new TeamAdapter());
        adapter.setOnItemClickListener((item, position) -> showEditTeamDialog(item));

        presenter.attachView(this);
    }


    //region ActionBar Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.teams, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showEditTeamDialog(null);
                return true;

            case R.id.action_clear:
                presenter.clear();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void updateTeams(List<Team> teams) {
        adapter.clear();
        adapter.addAll(teams);
    }


    public void showEditTeamDialog(Team team) {
        EditTeamDialogFragment.newInstance(team).show(getChildFragmentManager(), "add_team");
    }
}