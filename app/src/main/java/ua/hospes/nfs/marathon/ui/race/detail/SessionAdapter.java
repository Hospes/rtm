package ua.hospes.nfs.marathon.ui.race.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.adapter.AbsRecyclerAdapter;
import ua.hospes.nfs.marathon.domain.cars.models.Car;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.utils.TimeUtils;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class SessionAdapter extends AbsRecyclerAdapter<Session, SessionAdapter.MyHolder> {
    private long startTime = -1;


    public SessionAdapter(long startTime) {
        this.startTime = startTime;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_race_detail_session, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.itemView.setBackgroundResource(position % 2 == 0 ? R.drawable.bg_race_item_transparent : R.drawable.bg_race_item_detail_not_trans);

        Session item = getItem(position);

        Car car = item.getCar();
        holder.car.setText(car == null ? "" : String.valueOf(car.getNumber()));
        if (startTime == -1 || item.getStartDurationTime() == -1) {
            holder.start.setText("");
        } else {
            holder.start.setText(TimeUtils.formatNanoWithMills(item.getStartDurationTime() - startTime));
        }
        holder.driver.setText(item.getDriver() == null ? "" : item.getDriver().getName());
        if (item.getStartDurationTime() == -1 || item.getEndDurationTime() == -1)
            holder.duration.setText("NOW");
        else
            holder.duration.setText(TimeUtils.formatNanoWithMills(item.getEndDurationTime() - item.getStartDurationTime()));
        holder.type.setText(item.getType().getTitle());
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView driver, car, start, duration, type;

        public MyHolder(View itemView) {
            super(itemView);

            start = UiUtils.findView(itemView, R.id.start);
            duration = UiUtils.findView(itemView, R.id.duration);
            car = UiUtils.findView(itemView, R.id.car);
            driver = UiUtils.findView(itemView, R.id.driver);
            type = UiUtils.findView(itemView, R.id.type);
        }
    }
}