package ua.hospes.nfs.marathon.ui.drivers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.RxUtils;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class AddDriverDialogFragment extends DialogFragment {
    private static final String KEY_DRIVER = "driver";

    @Inject DriversRepository repository;

    private TextInputEditText name;
    private Driver driver = null;


    public static AddDriverDialogFragment newInstance(Driver driver) {
        AddDriverDialogFragment frag = new AddDriverDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_DRIVER, driver);
        frag.setArguments(args);

        return frag;
    }

    public AddDriverDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);

        if (getArguments() != null) {
            driver = getArguments().getParcelable(KEY_DRIVER);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_driver, null);

        name = UiUtils.findView(view, R.id.name);

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


    private DialogInterface.OnClickListener onOkClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (driver == null) {
                driver = new Driver(name.getText().toString(), -1);
            } else {
                driver.setName(name.getText().toString());
            }
            repository.save(driver).compose(RxUtils.applySchedulers()).subscribe();
        }
    };

    private DialogInterface.OnClickListener onCancelClick = (dialogInterface, i) -> {
        //Do nothing
    };

    private DialogInterface.OnClickListener onDeleteClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (driver == null) return;
            repository.delete(driver).compose(RxUtils.applySchedulers()).subscribe();
        }
    };
}