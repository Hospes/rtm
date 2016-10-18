package ua.hospes.nfs.marathon.ui.race.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.adapter.AbsRecyclerAdapter;
import ua.hospes.nfs.marathon.domain.race.models.DriverDetails;
import ua.hospes.nfs.marathon.ui.race.widgets.DriverTimeView;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class DriverDetailsAdapter extends AbsRecyclerAdapter<DriverDetails, DriverDetailsAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver_details, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        DriverDetails item = getItem(position);

        holder.name.setText(item.getName());
        holder.driver.setSession(item.getSession());
        holder.driver.setPrevDuration(item.getPrevDuration());
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private DriverTimeView driver;

        public MyHolder(View itemView) {
            super(itemView);

            name = UiUtils.findView(itemView, R.id.name);
            driver = UiUtils.findView(itemView, R.id.driver);
        }
    }
}