package ua.hospes.rtm.ui.race;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.race.RaceInteractor;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
public class SetCarDialogFragment extends AppCompatDialogFragment {
    private final static String KEY_SESSION_ID = "session_id";
    private final static String KEY_CARS = "cars";
    @Inject RaceInteractor raceInteractor;
    private final List<Car> cars = new ArrayList<>();
    private String[] titles = new String[]{};
    private int sessionId = -1;


    public static SetCarDialogFragment newInstance(int sessionId, List<Car> cars) {
        SetCarDialogFragment dialog = new SetCarDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SESSION_ID, sessionId);
        bundle.putParcelableArray(KEY_CARS, cars.toArray(new Car[cars.size()]));
        dialog.setArguments(bundle);

        return dialog;
    }

    public SetCarDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sessionId = getArguments().getInt(KEY_SESSION_ID, -1);

            Parcelable[] array = getArguments().getParcelableArray(KEY_CARS);
            Collections.addAll(cars, array == null ? new Car[]{} : (Car[]) array);

            titles = Collections2.transform(cars, input -> String.valueOf(input.getNumber())).toArray(new String[cars.size()]);
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
        return new AlertDialog.Builder(getActivity())
                .setTitle("Select car")
                .setItems(titles, (dialog, which) -> {
                    raceInteractor.setCar(sessionId, cars.get(which))
                            .compose(RxUtils.applySchedulers())
                            .subscribe(aBoolean -> {}, Throwable::printStackTrace);
                })
                .create();
    }
}