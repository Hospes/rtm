package ua.hospes.rtm.ui.race.detail;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.race.models.DriverDetails;
import ua.hospes.rtm.widgets.DriverTimeView;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
class DriverDetailsAdapter extends AbsRecyclerAdapter<DriverDetails, DriverDetailsAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_driver_details);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, DriverDetails item, int position) {
        Context context = holder.itemView.getContext();

        TypedValue bg1 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground1, bg1, false);
        TypedValue bg2 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground2, bg2, false);

        int resBG;
        if ((position / 2) % 2 == 0) {
            resBG = position % 2 == 0 ? bg1.data : bg2.data;
        } else {
            resBG = position % 2 == 0 ? bg2.data : bg1.data;
        }
        holder.itemView.setBackgroundResource(resBG);

        holder.name.setText(item.getName());
        holder.driver.setSession(item.getSession());
        holder.driver.setPrevDuration(item.getPrevDuration());
    }

    class MyHolder extends AbsRecyclerHolder {
        TextView name;
        DriverTimeView driver;

        MyHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        @Override
        protected void findViews(View itemView) {
            name = UiUtils.findView(itemView, R.id.name);
            driver = UiUtils.findView(itemView, R.id.driver);
        }
    }
}