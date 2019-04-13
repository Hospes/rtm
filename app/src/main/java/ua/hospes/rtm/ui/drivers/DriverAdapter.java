package ua.hospes.rtm.ui.drivers;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
class DriverAdapter extends AbsRecyclerAdapter<Driver, DriverAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_driver);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Driver item, int position) {
        Context context = holder.itemView.getContext();
        Resources res = holder.itemView.getResources();

        TypedValue bg1 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground1, bg1, false);
        TypedValue bg2 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground2, bg2, false);

        holder.itemView.setBackgroundResource(position % 2 == 0 ? bg2.data : bg1.data);

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