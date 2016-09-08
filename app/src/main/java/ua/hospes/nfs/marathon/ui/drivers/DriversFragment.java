package ua.hospes.nfs.marathon.ui.drivers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class DriversFragment extends Fragment implements DriversContract.View {
    @Inject DriversPresenter presenter;

    private RecyclerView rv;
    private DriverAdapter adapter;


    public static Fragment newInstance() {
        return new DriversFragment();
    }

    public DriversFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drivers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = UiUtils.findView(view, R.id.list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter = new DriverAdapter());
        adapter.setOnItemClickListener((item, position) -> showAddDriverDialog(item));

        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void updateDrivers(List<Driver> drivers) {
        adapter.clear();
        adapter.addAll(drivers);
    }


    public void showAddDriverDialog(Driver driver) {
        AddDriverDialogFragment dialog = AddDriverDialogFragment.newInstance(driver);
        dialog.show(getChildFragmentManager(), "add_driver");
    }
}