package ua.hospes.nfs.marathon.ui.teams;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;
import ua.hospes.nfs.marathon.utils.RxUtils;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class EditTeamDialogFragment extends AppCompatDialogFragment {
    private static final String KEY_TEAM = "team";

    @Inject TeamsRepository repository;

    private TextInputEditText name;
    private Team team = null;


    public static EditTeamDialogFragment newInstance(Team team) {
        EditTeamDialogFragment frag = new EditTeamDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_TEAM, team);
        frag.setArguments(args);

        return frag;
    }

    public EditTeamDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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

        name = UiUtils.findView(view, R.id.name);

        if (team != null) {
            name.setText(team.getName());
            name.setSelection(team.getName().length());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, onOkClick)
                .setNegativeButton(android.R.string.cancel, onCancelClick);

        if (team != null) {
            builder.setNeutralButton(R.string.delete, onDeleteClick);
        }

        return builder.create();
    }


    private DialogInterface.OnClickListener onOkClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (team == null) {
                team = new Team(name.getText().toString());
            } else {
                team.setName(name.getText().toString());
            }
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
            if (team == null) return;
            repository.delete(team)
                    .compose(RxUtils.applySchedulers())
                    .subscribe(aBoolean -> {}, Throwable::printStackTrace);
        }
    };
}