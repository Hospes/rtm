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
import ua.hospes.nfs.marathon.domain.cars.models.Car;
import ua.hospes.nfs.marathon.domain.race.RaceInteractor;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class SetCarDialog extends DialogFragment {
    private final static String KEY_SESSION_ID = "session_id";
    private final static String KEY_CARS = "cars";
    @Inject RaceInteractor raceInteractor;
    private final List<Car> cars = new ArrayList<>();
    private String[] titles = new String[]{};
    private int sessionId = -1;


    public static SetCarDialog newInstance(int sessionId, List<Car> cars) {
        SetCarDialog dialog = new SetCarDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SESSION_ID, sessionId);
        bundle.putParcelableArray(KEY_CARS, cars.toArray(new Car[cars.size()]));
        dialog.setArguments(bundle);

        return dialog;
    }

    public SetCarDialog() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);

        if (getArguments() != null) {
            sessionId = getArguments().getInt(KEY_SESSION_ID, -1);

            Parcelable[] array = getArguments().getParcelableArray(KEY_CARS);
            Collections.addAll(cars, array == null ? new Car[]{} : (Car[]) array);

            titles = Collections2.transform(cars, input -> String.valueOf(input.getNumber())).toArray(new String[cars.size()]);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Select car")
                .setItems(titles, (dialog, which) -> {
                    raceInteractor.setCar(sessionId, cars.get(which))
                            .compose(RxUtils.applySchedulers())
                            .subscribe();
                })
                .create();
    }
}