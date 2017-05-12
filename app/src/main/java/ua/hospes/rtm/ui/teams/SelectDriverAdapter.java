package ua.hospes.rtm.ui.teams;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.google.common.collect.ImmutableList;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.widgets.CheckableFrameLayout;

/**
 * @author Andrew Khloponin
 */
class SelectDriverAdapter extends AbsRecyclerAdapter<Driver, SelectDriverAdapter.MyHolder> {
    private final Set<Integer> selectedIds = new HashSet<>();


    SelectDriverAdapter(@Nullable Driver[] drivers) {
        if (drivers == null) return;
        for (Driver driver : drivers) selectedIds.add(driver.getId());
    }


    @Override
    public void onBindViewHolder(MyHolder holder, Driver item, int position) {
        holder.text.setText(item.getName());
        holder.text.setChecked(selectedIds.contains(item.getId()));
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_driver_selectable);
    }

    class MyHolder extends AbsRecyclerHolder {
        AppCompatCheckBox text;

        MyHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);

            itemView.setOnClickListener(this::initClickListener);
            // Safe cast, we already knew what view are as root element
            ((CheckableFrameLayout) itemView).setOnCheckedChangeListener(this::initCheckChangeListener);
        }

        @Override
        protected void findViews(View itemView) {
            text = findView(R.id.text);
        }

        void initClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            Driver item = getItem(position);
            boolean alreadyChecked = selectedIds.contains(item.getId());
            boolean allowCheckChange = onAllowItemCheck(item, position, alreadyChecked);

            if (allowCheckChange) {
                if (alreadyChecked) {
                    selectedIds.remove(item.getId());
                    ((Checkable) view).setChecked(false);
                } else {
                    selectedIds.add(item.getId());
                    ((Checkable) view).setChecked(true);
                }

                notifyItemChanged(position);
            }

            onItemClick(item, position);
        }

        void initCheckChangeListener(boolean checked) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            onItemCheck(getItem(position), position, checked);
        }
    }


    Observable<Driver> emmitSelectedDrivers() {
        ImmutableList<Integer> immIds = ImmutableList.copyOf(selectedIds);
        return Observable.from(ImmutableList.copyOf(getItems()))
                .filter(driver -> immIds.contains(driver.getId()));
    }
}