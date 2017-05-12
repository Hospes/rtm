package ua.hospes.rtm.ui.teams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.utils.RxUtils;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class SelectDriversDialogFragment extends AppCompatDialogFragment {
    private static final String DRIVERS = "drivers";
    @Inject DriversRepository driversRepository;


    private Button btnOk, btnCancel;
    private RecyclerView rv;
    private SelectDriverAdapter adapter;
    @Nullable private Driver[] drivers = null;


    public static DialogFragment newInstance(@Nullable Driver[] driver) {
        SelectDriversDialogFragment frag = new SelectDriversDialogFragment();

        Bundle args = new Bundle();
        args.putParcelableArray(DRIVERS, driver);
        frag.setArguments(args);

        return frag;
    }

    public SelectDriversDialogFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drivers = (Driver[]) getArguments().getParcelableArray(DRIVERS);
        }
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_select_drivers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = UiUtils.findView(view, R.id.list);
        btnOk = UiUtils.findView(view, R.id.ok);
        btnCancel = UiUtils.findView(view, R.id.cancel);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnOk.setOnClickListener(this::dialogOk);
        btnCancel.setOnClickListener(this::dialogCancel);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter = new SelectDriverAdapter(drivers));

        RxUtils.manage(this, driversRepository.get()
                .toList()
                .compose(RxUtils.applySchedulers())
                .subscribe(this::updateDrivers, Throwable::printStackTrace));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxUtils.unsubscribe(this);
    }

    private void updateDrivers(List<Driver> drivers) {
        adapter.clear();
        adapter.addAll(drivers);
    }

    private void dialogOk(View view) {
        RxUtils.manage(this, adapter.emmitSelectedDrivers()
                .toList()
                .map(drivers -> drivers.toArray(new Driver[drivers.size()]))
                .compose(RxUtils.applySchedulers())
                .subscribe(drivers -> {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra("drivers", drivers));
                }, Throwable::printStackTrace));
        dismiss();
    }

    private void dialogCancel(View view) {
        dismiss();
    }
}