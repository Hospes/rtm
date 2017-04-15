package ua.hospes.rtm.ui.cars;

import android.app.Dialog;
import android.content.Context;
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

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.cars.CarsRepository;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.utils.RxUtils;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class EditCarDialogFragment extends DialogFragment {
    private static final String KEY_CAR = "car";

    @Inject CarsRepository carsRepository;

    private String[] ratingTitles = new String[]{"0", "1", "2"};

    private AppCompatSpinner spRating;
    private EditText etNumber;
    private Car car = null;
    private int selectedRating = -1;


    public static EditCarDialogFragment newInstance(Car car) {
        EditCarDialogFragment frag = new EditCarDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_CAR, car);
        frag.setArguments(args);

        return frag;
    }

    public EditCarDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            car = getArguments().getParcelable(KEY_CAR);
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_car, null);

        findViews(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ratingTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRating.setAdapter(adapter);
        spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRating = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRating = -1;
            }
        });

        if (car != null) {
            etNumber.setText(String.valueOf(car.getNumber()));
            etNumber.setSelection(String.valueOf(car.getNumber()).length());
            if (car.getRating() >= 0 && car.getRating() < 3)
                spRating.setSelection(car.getRating());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, onOkClick)
                .setNegativeButton(android.R.string.cancel, onCancelClick);

        if (car != null) {
            builder.setNeutralButton(R.string.delete, onDeleteClick);
        }

        return builder.create();
    }


    private void findViews(View view) {
        etNumber = UiUtils.findView(view, R.id.number);
        spRating = UiUtils.findView(view, R.id.rating);
    }

    private DialogInterface.OnClickListener onOkClick = (dialog, i) -> {
        if (car == null) car = new Car();

        try {
            car.setNumber(Integer.parseInt(etNumber.getText().toString()));
        } catch (NumberFormatException | NullPointerException e) {
            car.setNumber(0);
        }
        car.setRating(selectedRating);

        carsRepository.save(car)
                .compose(RxUtils.applySchedulers())
                .subscribe(aBoolean -> {}, Throwable::printStackTrace);
    };

    private DialogInterface.OnClickListener onCancelClick = (dialog, i) -> {/* Do nothing */};

    private DialogInterface.OnClickListener onDeleteClick = (dialog, i) -> {
        if (car == null) return;
        carsRepository.delete(car)
                .compose(RxUtils.applySchedulers())
                .subscribe(aBoolean -> {}, Throwable::printStackTrace);
    };
}