package ua.hospes.rtm.ui.race.detail;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.cars.Car;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.utils.TimeUtils;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
class SessionAdapter extends AbsRecyclerAdapter<Session, SessionAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_race_detail_session);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Session item, int position) {
        Context context = holder.itemView.getContext();

        TypedValue bg1 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground1, bg1, false);
        TypedValue bg2 = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listItemBackground2, bg2, false);

        holder.itemView.setBackgroundResource(position % 2 == 0 ? bg1.data : bg2.data);


        Car car = item.getCar();
        holder.car.setText(car == null ? "" : String.valueOf(car.getNumber()));
        if (item.getRaceStartTime() == -1 || item.getStartDurationTime() == -1) {
            holder.start.setText("");
        } else {
            holder.start.setText(TimeUtils.formatNanoWithMills(item.getStartDurationTime() - item.getRaceStartTime()));
        }
        holder.driver.setText(item.getDriver() == null ? "" : item.getDriver().getName());
        if (item.getStartDurationTime() == -1 || item.getEndDurationTime() == -1)
            holder.duration.setText(R.string.race_now);
        else
            holder.duration.setText(TimeUtils.formatNanoWithMills(item.getEndDurationTime() - item.getStartDurationTime()));
        holder.type.setText(item.getType().getTitle());
    }

    class MyHolder extends AbsRecyclerHolder {
        TextView driver, car, start, duration, type;

        MyHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        @Override
        protected void findViews(View itemView) {
            start = UiUtils.findView(itemView, R.id.start);
            duration = UiUtils.findView(itemView, R.id.duration);
            car = UiUtils.findView(itemView, R.id.car);
            driver = UiUtils.findView(itemView, R.id.driver);
            type = UiUtils.findView(itemView, R.id.type);
        }
    }
}