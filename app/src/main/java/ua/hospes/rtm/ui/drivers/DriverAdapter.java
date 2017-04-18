package ua.hospes.rtm.ui.drivers;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class DriverAdapter extends AbsRecyclerAdapter<Driver, DriverAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_driver);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Driver item, int position) {
        Resources res = holder.itemView.getResources();
        holder.itemView.setBackgroundResource(position % 2 == 0 ? R.drawable.bg_list_item_2 : R.drawable.bg_list_item_1);

        holder.name.setText(item.getName());
        holder.team.setText(TextUtils.isEmpty(item.getTeamName()) ? res.getString(R.string.drivers_no_team) : item.getTeamName());
    }


    class MyHolder extends AbsRecyclerHolder {
        private TextView name, team;

        MyHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);

            itemView.setOnClickListener(this::initClickListener);
        }


        @Override
        protected void findViews(View itemView) {
            name = UiUtils.findView(itemView, R.id.name);
            team = UiUtils.findView(itemView, R.id.team);
        }

        void initClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (getOnItemClickListener() == null) return;
            onItemClick(getItem(position), position);
        }
    }
}