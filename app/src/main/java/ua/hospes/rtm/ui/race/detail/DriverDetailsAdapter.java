package ua.hospes.rtm.ui.race.detail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.race.models.DriverDetails;
import ua.hospes.rtm.ui.race.widgets.DriverTimeView;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class DriverDetailsAdapter extends AbsRecyclerAdapter<DriverDetails, DriverDetailsAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_driver_details);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, DriverDetails item, int position) {
        int resBG;
        if ((position / 2) % 2 == 0) {
            resBG = position % 2 == 0 ? R.drawable.bg_list_item_1 : R.drawable.bg_race_item_detail_not_trans;
        } else {
            resBG = position % 2 == 0 ? R.drawable.bg_race_item_detail_not_trans : R.drawable.bg_list_item_1;
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