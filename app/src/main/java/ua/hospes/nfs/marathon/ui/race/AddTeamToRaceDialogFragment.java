package ua.hospes.nfs.marathon.ui.race;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.race.RaceRepository;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.team.models.Team;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class AddTeamToRaceDialogFragment extends DialogFragment {
    private static final String KEY_TEAMS = "teams";
    @Inject RaceRepository repository;
    private final List<Team> teams = new ArrayList<>();
    private String[] titles = new String[]{"No items"};
    private Team selected = null;


    public static AddTeamToRaceDialogFragment newInstance(List<Team> list) {
        AddTeamToRaceDialogFragment fragment = new AddTeamToRaceDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArray(KEY_TEAMS, list.toArray(new Team[list.size()]));
        fragment.setArguments(bundle);

        return fragment;
    }

    public AddTeamToRaceDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);

        if (getArguments() != null) {
            Collections.addAll(teams, (Team[]) getArguments().getParcelableArray(KEY_TEAMS));

            titles = Collections2.transform(teams, Team::getName).toArray(new String[teams.size()]);
            if (teams.size() > 0) selected = teams.get(0);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Add team to race")
                .setSingleChoiceItems(titles, 0, (dialog, which) -> selected = teams.get(which))
                .setPositiveButton(android.R.string.ok, onOkClick)
                .setNegativeButton(android.R.string.cancel, onCancelClick)
                .setNeutralButton(R.string.add_all, onAddAllClick)
                .create();
    }


    private DialogInterface.OnClickListener onOkClick = (dialogInterface, i) -> {
        if (selected == null) return;
        repository.addNew(new RaceItem(selected))
                .compose(RxUtils.applySchedulers())
                .subscribe(success -> {
                    Toast.makeText(getContext(), success ? "Team added to race!" : "Error while adding team to race", Toast.LENGTH_LONG).show();
                }, Throwable::printStackTrace);
    };

    private DialogInterface.OnClickListener onCancelClick = (dialogInterface, i) -> {/* Do nothing */};

    private DialogInterface.OnClickListener onAddAllClick = (dialogInterface, i) -> {
        repository.addNew(Collections2.transform(teams, RaceItem::new).toArray(new RaceItem[teams.size()]))
                .compose(RxUtils.applySchedulers())
                .subscribe(success -> {
                    Toast.makeText(getContext(), success ? "All teams added to race!" : "Error while adding team to race", Toast.LENGTH_LONG).show();
                }, Throwable::printStackTrace);
    };
}