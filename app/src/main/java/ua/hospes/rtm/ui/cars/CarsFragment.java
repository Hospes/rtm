package ua.hospes.rtm.ui.cars;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.core.ui.AbsFragment;
import ua.hospes.rtm.core.ui.SpaceItemDecoration;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class CarsFragment extends AbsFragment implements CarsContract.View {
    @Inject CarsPresenter presenter;

    private RecyclerView rv;
    private CarsAdapter adapter;


    public static Fragment newInstance() {
        return new CarsFragment();
    }

    public CarsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    protected int setActionBarTitle() {
        return R.string.cars_title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cars, container, false);
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
        rv.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.cars_column_count)));
        rv.addItemDecoration(new SpaceItemDecoration(getResources(), R.dimen.grid_item_margin));
        rv.setAdapter(adapter = new CarsAdapter());
        adapter.setOnItemClickListener((item, position) -> showEditCarDialog(item));

        presenter.attachView(this);
    }


    //region ActionBar Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cars, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showEditCarDialog(null);
                return true;

            case R.id.action_clear:
                showClearDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void updateCars(List<Car> cars) {
        adapter.clear();
        adapter.addAll(cars);
    }


    private void showEditCarDialog(Car car) {
        EditCarDialogFragment.newInstance(car).show(getChildFragmentManager(), "add_car");
    }

    private void showClearDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.cars_remove_all)
                .setPositiveButton(R.string.yes, (dialog, which) -> presenter.clear())
                .setNegativeButton(R.string.no, (dialog, which) -> {})
                .show();
    }
}