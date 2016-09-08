package ua.hospes.nfs.marathon.ui.teams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.team.models.Team;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
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

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);
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
        adapter.setOnItemClickListener((item, position) -> showAddTeamDialog(item));

        presenter.attachView(this);
    }

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


    public void showAddTeamDialog(Team team) {
        AddTeamDialogFragment.newInstance(team).show(getChildFragmentManager(), "add_team");
    }
}