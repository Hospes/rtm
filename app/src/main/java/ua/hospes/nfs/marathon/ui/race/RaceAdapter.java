package ua.hospes.nfs.marathon.ui.race;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.adapter.AbsRecyclerAdapter;
import ua.hospes.nfs.marathon.domain.cars.models.Car;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.ui.race.widgets.DriverTimeView;
import ua.hospes.nfs.marathon.ui.race.widgets.SessionTimeView;
import ua.hospes.nfs.marathon.ui.race.widgets.TimeView;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class RaceAdapter extends AbsRecyclerAdapter<RaceItem, RaceAdapter.MyHolder> {
    public static final float AUTO_PLAY_AREA_START_PADDING_RELATIVE = 0.3f;
    public static final float AUTO_PLAY_AREA_END_PADDING_RELATIVE = 0.3f;
    private RecyclerView rv;
    private OnItemClickListener<RaceItem> onSetCarClickListener;
    private OnItemClickListener<RaceItem> onSetDriverClickListener;
    private OnItemClickListener<RaceItem> onPitClickListener;
    private OnItemClickListener<RaceItem> onOutClickListener;


    public RaceAdapter(RecyclerView rv) {
        this.rv = rv;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_race, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.itemView.setBackgroundResource(position % 2 == 0 ? R.drawable.bg_race_item_transparent : R.drawable.bg_race_item_not_transparent);

        RaceItem item = getItem(position);

        holder.team.setText(String.format(Locale.getDefault(), "%1$d - %2$s", item.getTeamNumber(), item.getTeam().getName()));
        holder.pits.setText(String.valueOf(item.getDetails().getPitStops()));

        Session session = item.getSession();
        holder.driverTimeView.setSession(session);
        holder.sessionTimeView.setSession(session);
        if (session != null) {
            Car car = session.getCar();
            if (car != null) {
                holder.btnSetCar.setVisibility(View.GONE);
                holder.cSessionCar.setVisibility(View.VISIBLE);

                holder.btnSessionCar.setText(String.valueOf(car.getNumber()));
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
                holder.btnSessionCar.setTextColor(color);
            } else {
                holder.btnSetCar.setVisibility(View.VISIBLE);
                holder.cSessionCar.setVisibility(View.GONE);
            }

            Driver driver = session.getDriver();
            if (driver != null) {
                holder.btnSetDriver.setVisibility(View.GONE);
                holder.cSessionDriver.setVisibility(View.VISIBLE);

                holder.driver.setText(session.getDriver().getName());
                holder.driverTimeView.setPrevDuration(item.getDetails().getDriverDuration(driver.getId()));
            } else {
                holder.btnSetDriver.setVisibility(View.VISIBLE);
                holder.cSessionDriver.setVisibility(View.GONE);
            }
            holder.sessionType.setText(session.getType().getTitle());
            holder.sessionType.setVisibility(View.VISIBLE);
            switch (session.getType()) {
                case TRACK:
                    holder.sessionType.setTextColor(Color.GREEN);
                    holder.sessionTimeView.setTextColor(Color.GREEN);
                    holder.pit.setVisibility(View.VISIBLE);
                    holder.out.setVisibility(View.GONE);
                    break;

                case PIT:
                    holder.sessionType.setTextColor(Color.RED);
                    holder.sessionTimeView.setTextColor(Color.RED);
                    holder.pit.setVisibility(View.GONE);
                    holder.out.setVisibility(View.VISIBLE);
                    break;

                default:
                    holder.pit.setVisibility(View.GONE);
                    holder.out.setVisibility(View.GONE);
                    break;
            }
        }

        holder.btnSessionCar.setOnClickListener(v -> {
            if (onSetCarClickListener != null) onSetCarClickListener.onItemClick(item, position);
        });
        holder.btnSetCar.setOnClickListener(v -> {
            if (onSetCarClickListener != null) onSetCarClickListener.onItemClick(item, position);
        });

        holder.driver.setOnClickListener(v -> {
            if (onSetDriverClickListener != null) onSetDriverClickListener.onItemClick(item, position);
        });
        holder.btnSetDriver.setOnClickListener(v -> {
            if (onSetDriverClickListener != null) onSetDriverClickListener.onItemClick(item, position);
        });

        RxView.clicks(holder.pit).throttleFirst(5, TimeUnit.SECONDS).subscribe(v -> {
            if (onPitClickListener != null) onPitClickListener.onItemClick(item, position);
        });

        RxView.clicks(holder.out).throttleFirst(5, TimeUnit.SECONDS).subscribe(v -> {
            if (onOutClickListener != null) onOutClickListener.onItemClick(item, position);
        });

        holder.itemView.setOnClickListener(v -> {
            if (getOnItemClickListener() != null) getOnItemClickListener().onItemClick(item, position);
        });
    }


    public void updateDurations(long currentNanoTime) {
        for (TimeView tv : collectShouldPlayItems()) tv.setCurrentNanoTime(currentNanoTime);
    }

    private Set<TimeView> collectShouldPlayItems() {
        Set<TimeView> set = new HashSet<>();

        RecyclerView.LayoutManager lm = rv.getLayoutManager();

        int autoPlayAreaStart = (int) (rv.getTop() + rv.getHeight() * AUTO_PLAY_AREA_START_PADDING_RELATIVE);
        int autoPlayAreaEnd = (int) (rv.getBottom() - rv.getHeight() * AUTO_PLAY_AREA_END_PADDING_RELATIVE);

        int count = lm.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = lm.getChildAt(i);
            int viewStart = lm.getDecoratedTop(child);
            int viewEnd = lm.getDecoratedBottom(child);

            boolean shouldPlay = false;
            shouldPlay = shouldPlay || (rv.getTop() <= viewStart && rv.getBottom() >= viewEnd); // completely visible
            shouldPlay = shouldPlay || !(autoPlayAreaStart > viewEnd || autoPlayAreaEnd < viewStart); // near center;

            if (shouldPlay) {
                MyHolder viewHolder = (MyHolder) rv.getChildViewHolder(child);
                set.add(viewHolder.sessionTimeView);
                set.add(viewHolder.driverTimeView);
            }
        }
        return set;
    }


    public void setOnSetCarClickListener(OnItemClickListener<RaceItem> onSetCarClickListener) {
        this.onSetCarClickListener = onSetCarClickListener;
    }

    public void setOnSetDriverClickListener(OnItemClickListener<RaceItem> onSetDriverClickListener) {
        this.onSetDriverClickListener = onSetDriverClickListener;
    }

    public void setOnPitClickListener(OnItemClickListener<RaceItem> onPitClickListener) {
        this.onPitClickListener = onPitClickListener;
    }

    public void setOnOutClickListener(OnItemClickListener<RaceItem> onOutClickListener) {
        this.onOutClickListener = onOutClickListener;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        //Car
        private View cSessionCar;
        private Button btnSessionCar;
        private Button btnSetCar;

        //Driver
        private View cSessionDriver;
        private DriverTimeView driverTimeView;
        private Button driver;
        private Button btnSetDriver;

        private TextView team, sessionType, pits;
        private SessionTimeView sessionTimeView;
        private Button pit, out;

        public MyHolder(View itemView) {
            super(itemView);

            //Car
            cSessionCar = UiUtils.findView(itemView, R.id.container_session_car);
            btnSessionCar = UiUtils.findView(itemView, R.id.btn_session_car);
            btnSetCar = UiUtils.findView(itemView, R.id.btn_set_car);

            team = UiUtils.findView(itemView, R.id.tv_team);
            sessionTimeView = UiUtils.findView(itemView, R.id.tv_session_duration);
            driver = UiUtils.findView(itemView, R.id.btn_session_driver);
            sessionType = UiUtils.findView(itemView, R.id.tv_session_type);

            pits = UiUtils.findView(itemView, R.id.tv_pits);

            driverTimeView = UiUtils.findView(itemView, R.id.tv_driver_all_time);

            btnSetDriver = UiUtils.findView(itemView, R.id.btn_set_driver);

            cSessionDriver = UiUtils.findView(itemView, R.id.container_session_driver);

            pit = UiUtils.findView(itemView, R.id.btn_pit);
            out = UiUtils.findView(itemView, R.id.btn_out);
        }
    }
}