package ua.hospes.nfs.marathon.ui.race.detail;

import android.graphics.Color;
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
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_race_detail_session, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Session item = getItem(position);

        Car car = item.getCar();
        if (car != null) {
            holder.car.setText(String.valueOf(car.getNumber()));
            int color;
            switch (car.getRating()) {
                case 0:
                    color = Color.RED;
                    break;
                case 1:
                    color = Color.YELLOW;
                    break;
                default:
                    color = Color.GREEN;
                    break;
            }
            holder.car.setTextColor(color);
        } else {
            holder.car.setText("");
        }

        holder.driver.setText(item.getDriver() == null ? "" : item.getDriver().getName());
        if (item.getStartDurationTime() == -1 || item.getEndDurationTime() == -1)
            holder.duration.setText("NOW");
        else
            holder.duration.setText(TimeUtils.formatNanoWithMills(item.getEndDurationTime() - item.getStartDurationTime()));
        holder.type.setText(item.getType().getTitle());
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView driver, car, duration, type;

        public MyHolder(View itemView) {
            super(itemView);

            duration = UiUtils.findView(itemView, R.id.duration);
            car = UiUtils.findView(itemView, R.id.car);
            driver = UiUtils.findView(itemView, R.id.driver);
            type = UiUtils.findView(itemView, R.id.type);
        }
    }
}