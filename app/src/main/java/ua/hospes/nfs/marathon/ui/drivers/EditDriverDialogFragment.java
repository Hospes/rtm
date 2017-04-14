package ua.hospes.nfs.marathon.ui.drivers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;
import ua.hospes.nfs.marathon.utils.RxUtils;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class EditDriverDialogFragment extends AppCompatDialogFragment {
    private static final String KEY_DRIVER = "driver";

    @Inject DriversRepository driversRepository;
    @Inject TeamsRepository teamsRepository;

    private List<Team> teams = new ArrayList<>();
    private AppCompatSpinner spinner;
    private EditText name;
    private Driver driver = null;
    private Team team = null;


    public static EditDriverDialogFragment newInstance(Driver driver) {
        EditDriverDialogFragment frag = new EditDriverDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_DRIVER, driver);
        frag.setArguments(args);

        return frag;
    }

    public EditDriverDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            driver = getArguments().getParcelable(KEY_DRIVER);
        }
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_driver, null);

        findViews(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select team");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int realPos = position - 1;
                if (realPos < 0 || realPos >= teams.size()) team = null;
                else team = teams.get(realPos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                team = null;
            }
        });
        loadTeams(adapter);

        if (driver != null) {
            name.setText(driver.getName());
            name.setSelection(driver.getName().length());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, onOkClick)
                .setNegativeButton(android.R.string.cancel, onCancelClick);

        if (driver != null) {
            builder.setNeutralButton(R.string.delete, onDeleteClick);
        }

        return builder.create();
    }


    private void findViews(View view) {
        name = UiUtils.findView(view, R.id.name);
        spinner = UiUtils.findView(view, R.id.team);
    }

    private void loadTeams(ArrayAdapter<String> adapter) {
        teams.clear();
        adapter.clear();
        adapter.add("No team");
        teamsRepository.get()
                .compose(RxUtils.applySchedulers())
                .subscribe(team -> {
                    teams.add(team);
                    adapter.add(team.getName());
                    if (driver != null && driver.getTeamId() == team.getId()) {
                        spinner.setSelection(teams.size());
                    }
                }, Throwable::printStackTrace);
    }


    private DialogInterface.OnClickListener onOkClick = (dialog, i) -> {
        if (driver == null) {
            driver = new Driver(name.getText().toString());
        } else {
            driver.setName(name.getText().toString());
        }

        if (team != null) {
            driver.setTeamId(team.getId());
            driver.setTeamName(team.getName());
        } else {
            driver.setTeamId(-1);
            driver.setTeamName(null);
        }

        driversRepository.save(driver)
                .compose(RxUtils.applySchedulers())
                .subscribe(aBoolean -> {}, Throwable::printStackTrace);
    };

    private DialogInterface.OnClickListener onCancelClick = (dialog, i) -> {/* Do nothing */};

    private DialogInterface.OnClickListener onDeleteClick = (dialog, i) -> {
        if (driver == null) return;
        driversRepository.delete(driver)
                .compose(RxUtils.applySchedulers())
                .subscribe(aBoolean -> {}, Throwable::printStackTrace);
    };
}