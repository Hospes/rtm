package ua.hospes.rtm.ui.teams;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Collections2;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.team.models.Team;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class TeamAdapter extends AbsRecyclerAdapter<Team, TeamAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Team item, int position) {
        holder.id.setText(String.valueOf(item.getId()));
        holder.name.setText(item.getName());


        holder.drivers.setText(Collections2.transform(item.getDrivers(), Driver::getName).toString().replaceAll("(\\[|\\])", ""));

        holder.edit.setOnClickListener(view -> getOnItemClickListener().onItemClick(item, position));
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView id, name, drivers;
        private Button edit;

        public MyHolder(View itemView) {
            super(itemView);

            id = UiUtils.findView(itemView, R.id.id);
            name = UiUtils.findView(itemView, R.id.name);
            drivers = UiUtils.findView(itemView, R.id.drivers);
            edit = UiUtils.findView(itemView, R.id.edit);
        }
    }
}