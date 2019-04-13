package ua.hospes.rtm.ui.teams;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.Collections2;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.team.models.Team;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
class TeamAdapter extends AbsRecyclerAdapter<Team, TeamAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_team);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Team item, int position) {
        Context context = holder.itemView.getContext();

        TypedValue bg1 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground1, bg1, false);
        TypedValue bg2 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground2, bg2, false);

        holder.itemView.setBackgroundResource(position % 2 == 0 ? bg2.data : bg1.data);

        holder.name.setText(item.getName());
        holder.drivers.setText(Collections2.transform(item.getDrivers(), Driver::getName).toString().replaceAll("(\\[|\\])", ""));
    }


    class MyHolder extends AbsRecyclerHolder {
        private TextView name, drivers;

        MyHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);

            itemView.setOnClickListener(this::initClickListener);
        }

        @Override
        protected void findViews(View itemView) {
            name = UiUtils.findView(itemView, R.id.name);
            drivers = UiUtils.findView(itemView, R.id.drivers);
        }

        void initClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (getOnItemClickListener() == null) return;
            onItemClick(getItem(position), position);
        }
    }
}