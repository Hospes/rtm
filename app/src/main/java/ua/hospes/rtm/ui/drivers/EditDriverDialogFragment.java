package ua.hospes.rtm.ui.drivers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.team.TeamsRepository;
import ua.hospes.rtm.domain.team.models.Team;
import ua.hospes.rtm.utils.RxUtils;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class EditDriverDialogFragment extends AppCompatDialogFragment {
    private static final String KEY_DRIVER = "driver";

    @Inject
    DriversRepository driversRepository;
    @Inject
    TeamsRepository teamsRepository;

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

        name.setOnEditorActionListener((v, actionId, event) -> {
            boolean result = onEditorAction(v, actionId, event);
            if (result) {
                onOkClick.onClick(null, 0);
                dismiss();
            }
            return result;
        });

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


    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // If triggered by an enter key, this is the event; otherwise, this is null.
        if (event != null) {
            // if shift key is down, then we want to insert the '\n' char in the TextView;
            // otherwise, the default action is to send the message.
            if (!event.isShiftPressed()) {
//                if (isPreparedForSending()) {
//                    confirmSendMessageIfNeeded();
//                }
                return true;
            }
            return false;
        }

//        if (isPreparedForSending()) {
//            confirmSendMessageIfNeeded();
//        }
        return true;
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
        driversRepository.remove(driver.getId())
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(aBoolean -> {}, Throwable::printStackTrace);
    };
}