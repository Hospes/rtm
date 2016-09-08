package ua.hospes.nfs.marathon.ui.drivers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.adapter.AbsRecyclerAdapter;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class DriverAdapter extends AbsRecyclerAdapter<Driver, DriverAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Driver item = getItem(position);

        holder.id.setText(String.valueOf(item.getId()));
        holder.name.setText(item.getName());
        holder.team.setText(String.valueOf(item.getTeamId()));

        holder.edit.setOnClickListener(view -> getOnItemClickListener().onItemClick(item, position));
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView id, name, team;
        private Button edit;

        public MyHolder(View itemView) {
            super(itemView);

            id = UiUtils.findView(itemView, R.id.id);
            name = UiUtils.findView(itemView, R.id.name);
            team = UiUtils.findView(itemView, R.id.team);
            edit = UiUtils.findView(itemView, R.id.edit);
        }
    }
}