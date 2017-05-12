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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.cars.CarsRepository;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.cars.models.CarQuality;
import ua.hospes.rtm.utils.RxUtils;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class EditCarDialogFragment extends DialogFragment {
    private static final String KEY_CAR = "car";

    @Inject CarsRepository carsRepository;

    private final CarQuality[] qualities = {CarQuality.LOW, CarQuality.NORMAL, CarQuality.HIGH};

    private AppCompatSpinner spQuality;
    private EditText etNumber;
    private CompoundButton cbBroken;

    private Car car = null;
    private CarQuality selectedQuality = CarQuality.NORMAL;


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


        spQuality.setAdapter(new CarQualityAdapter(getContext(), qualities));
        spQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuality = (CarQuality) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedQuality = CarQuality.NORMAL;
            }
        });

        if (car != null) {
            etNumber.setText(String.valueOf(car.getNumber()));
            etNumber.setSelection(String.valueOf(car.getNumber()).length());
            selectedQuality = car.getQuality();
            cbBroken.setChecked(car.isBroken());
        }

        for (int i = 0; i < qualities.length; i++) {
            if (qualities[i].equals(selectedQuality)) spQuality.setSelection(i);
        }

        etNumber.setOnEditorActionListener((v, actionId, event) -> {
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

        if (car != null) {
            builder.setNeutralButton(R.string.delete, onDeleteClick);
        }

        return builder.create();
    }


    private void findViews(View view) {
        etNumber = UiUtils.findView(view, R.id.number);
        spQuality = UiUtils.findView(view, R.id.quality);
        cbBroken = UiUtils.findView(view, R.id.broken);
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
        if (car == null) car = new Car();

        try {
            car.setNumber(Integer.parseInt(etNumber.getText().toString()));
        } catch (NumberFormatException | NullPointerException e) {
            car.setNumber(0);
        }
        car.setQuality(selectedQuality);
        car.setBroken(cbBroken.isChecked());

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