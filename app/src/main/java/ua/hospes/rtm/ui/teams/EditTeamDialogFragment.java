package ua.hospes.rtm.ui.teams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.collect.Collections2;

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
public class EditTeamDialogFragment extends AppCompatDialogFragment {
    private static final int REQUEST_SELECT_DIRVERS = 1;
    private static final String KEY_TEAM = "team";

    @Inject DriversRepository driversRepository;
    @Inject TeamsRepository repository;

    private EditText name;
    private TextView drivers;
    private Button btnAssignDrivers;
    @NonNull private Team team = new Team();


    public static EditTeamDialogFragment newInstance(@Nullable Team team) {
        EditTeamDialogFragment frag = new EditTeamDialogFragment();

        Bundle args = new Bundle();
        if (team != null) args.putParcelable(KEY_TEAM, team);
        frag.setArguments(args);

        return frag;
    }

    public EditTeamDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(KEY_TEAM)) {
            team = getArguments().getParcelable(KEY_TEAM);
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_team, null);
        findViews(view);

        if (team.getId() != -1) {
            name.setText(team.getName());
            name.setSelection(team.getName().length());
            updateDrivers(team.getDrivers());
        }

        btnAssignDrivers.setOnClickListener(v -> showSelectDriversDialog());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, onOkClick)
                .setNegativeButton(android.R.string.cancel, onCancelClick);

        if (team.getId() != -1) {
            builder.setNeutralButton(R.string.delete, onDeleteClick);
        }

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_DIRVERS:
                if (resultCode != Activity.RESULT_OK) break;
                team.setDrivers((Driver[]) data.getExtras().getParcelableArray("drivers"));
                updateDrivers(team.getDrivers());
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxUtils.unsubscribe(this);
    }


    private void findViews(View view) {
        name = UiUtils.findView(view, R.id.name);
        drivers = UiUtils.findView(view, R.id.drivers);
        btnAssignDrivers = UiUtils.findView(view, R.id.assign_drivers);
    }

    private void showSelectDriversDialog() {
        DialogFragment dialog = SelectDriversDialogFragment.newInstance(team.getDrivers().toArray(new Driver[team.getDrivers().size()]));
        dialog.setTargetFragment(this, REQUEST_SELECT_DIRVERS);
        dialog.show(getChildFragmentManager(), "select_drivers");
    }

    private void updateDrivers(List<Driver> drivers) {
        this.drivers.setText(
                drivers.isEmpty()
                        ? "No drivers"
                        : Collections2.transform(drivers, Driver::getName).toString().replaceAll("(\\[|\\])", "")
        );
    }


    private DialogInterface.OnClickListener onOkClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            team.setName(name.getText().toString());
            repository.save(team)
                    .compose(RxUtils.applySchedulers())
                    .subscribe(aBoolean -> {}, Throwable::printStackTrace);
        }
    };

    private DialogInterface.OnClickListener onCancelClick = (dialogInterface, i) -> {
        //Do nothing
    };

    private DialogInterface.OnClickListener onDeleteClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            repository.remove(team)
                    .compose(RxUtils.applySchedulersSingle())
                    .subscribe(aBoolean -> {}, Throwable::printStackTrace);
        }
    };
}