package ua.hospes.nfs.marathon.ui.race;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.race.RaceRepository;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.RxUtils;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class AddTeamToRaceDialogFragment extends DialogFragment {
    @Inject RaceRepository raceRepository;
    @Inject SessionsRepository sessionsRepository;
    @Inject TeamsRepository teamsRepository;
    private final List<Team> teams = new ArrayList<>();

    private AppCompatSpinner spinner;
    private EditText etNumber;
    private Team team = null;


    public static AddTeamToRaceDialogFragment newInstance() {
        return new AddTeamToRaceDialogFragment();
    }

    public AddTeamToRaceDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_race_item, null);

        findViews(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select team");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || position >= teams.size()) team = null;
                else team = teams.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                team = null;
            }
        });
        loadTeams(adapter);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, onOkClick)
                .setNegativeButton(android.R.string.cancel, onCancelClick)
                .create();
    }


    private void findViews(View view) {
        etNumber = UiUtils.findView(view, R.id.number);
        spinner = UiUtils.findView(view, R.id.team);
    }

    private void loadTeams(ArrayAdapter<String> adapter) {
        teams.clear();
        adapter.clear();

        teamsRepository.getNotInRace()
                .compose(RxUtils.applySchedulers())
                .subscribe(team -> {
                    teams.add(team);
                    adapter.add(team.getName());
                    if (teams.size() != 0) spinner.setSelection(0);
                });
    }


    private DialogInterface.OnClickListener onOkClick = (dialogInterface, i) -> {
        int number = -1;
        try {
            number = Integer.parseInt(etNumber.getText().toString());
        } catch (NumberFormatException e) {/* do nothing */}
        if (team == null || number == -1) return;

        Observable.zip(
                Observable.just(team),
                Observable.just(number),
                sessionsRepository.newSessions(Session.Type.TRACK, team.getId()),
                (team1, integer, session) -> {
                    RaceItem raceItem = new RaceItem(team1);
                    raceItem.setTeamNumber(integer);
                    raceItem.setSession(session);
                    return raceItem;
                }
        )
                .flatMap(raceRepository::addNew)
                .compose(RxUtils.applySchedulers())
                .subscribe(success -> {
                    Toast.makeText(getContext(), success ? "Team added to race!" : "Error while adding team to race", Toast.LENGTH_LONG).show();
                }, Throwable::printStackTrace);
    };

    private DialogInterface.OnClickListener onCancelClick = (dialogInterface, i) -> {/* Do nothing */};
}