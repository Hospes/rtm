package ua.hospes.nfs.marathon.ui.race;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.adapter.AbsRecyclerAdapter;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class RaceAdapter extends AbsRecyclerAdapter<RaceItem, RaceAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_race, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        RaceItem item = getItem(position);

        holder.id.setText(String.valueOf(item.getId()));
        holder.teamName.setText(item.getTeam().getName());
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView id, teamName;

        public MyHolder(View itemView) {
            super(itemView);

            id = UiUtils.findView(itemView, R.id.tv_id);
            teamName = UiUtils.findView(itemView, R.id.tv_team_name);
        }
    }
}