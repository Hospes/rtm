package ua.hospes.nfs.marathon.ui.race;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.race.RaceInteractor;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class SetDriverDialog extends DialogFragment {
    private final static String KEY_SESSION_ID = "session_id";
    private final static String KEY_TEAM_ID = "team_id";
    private final static String KEY_DRIVERS = "drivers";
    @Inject RaceInteractor raceInteractor;
    private final List<Driver> drivers = new ArrayList<>();
    private String[] titles = new String[]{};
    private int sessionId = -1;
    private int teamId = -1;


    public static SetDriverDialog newInstance(int sessionId, int teamId, List<Driver> drivers) {
        SetDriverDialog dialog = new SetDriverDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SESSION_ID, sessionId);
        bundle.putInt(KEY_TEAM_ID, teamId);
        bundle.putParcelableArray(KEY_DRIVERS, drivers.toArray(new Driver[drivers.size()]));
        dialog.setArguments(bundle);

        return dialog;
    }

    public SetDriverDialog() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);

        if (getArguments() != null) {
            sessionId = getArguments().getInt(KEY_SESSION_ID, -1);
            teamId = getArguments().getInt(KEY_TEAM_ID, -1);

            Parcelable[] array = getArguments().getParcelableArray(KEY_DRIVERS);
            Collections.addAll(drivers, array == null ? new Driver[]{} : (Driver[]) array);

            titles = Collections2.transform(drivers, Driver::getName).toArray(new String[drivers.size()]);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Select driver")
                .setItems(titles, (dialog, which) -> {
                    Driver driver = drivers.get(which);
                    raceInteractor.setDriver(sessionId, teamId, driver.getId())
                            .compose(RxUtils.applySchedulers())
                            .subscribe(aBoolean -> {}, Throwable::printStackTrace);
                })
                .create();
    }
}