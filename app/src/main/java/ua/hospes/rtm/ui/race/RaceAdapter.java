package ua.hospes.rtm.ui.race;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jakewharton.rxbinding.view.RxView;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.OnItemClickListener;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.preferences.PreferencesManager;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.ui.race.widgets.DriverTimeView;
import ua.hospes.rtm.ui.race.widgets.SessionTimeView;
import ua.hospes.rtm.ui.race.widgets.TimeView;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class RaceAdapter extends AbsRecyclerAdapter<RaceItem, RaceAdapter.MyHolder> {
    public static final float AUTO_PLAY_AREA_START_PADDING_RELATIVE = 0.3f;
    public static final float AUTO_PLAY_AREA_END_PADDING_RELATIVE = 0.3f;
    private final boolean isPitStopsRemoved;
    private RecyclerView rv;
    private OnItemClickListener<RaceItem> onSetCarClickListener;
    private OnItemClickListener<RaceItem> onSetDriverClickListener;
    private OnItemClickListener<RaceItem> onPitClickListener;
    private OnItemClickListener<RaceItem> onOutClickListener;


    public RaceAdapter(RecyclerView rv, @NonNull PreferencesManager preferencesManager) {
        this.rv = rv;
        this.isPitStopsRemoved = preferencesManager.isPitStopSessionsRemoved();
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_race, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, RaceItem item, int position) {
        Context context = holder.itemView.getContext();
        holder.itemView.setBackgroundResource(position % 2 == 0 ? R.drawable.bg_race_item_transparent : R.drawable.bg_race_item_not_transparent);

        holder.team.setText(String.format(Locale.getDefault(), "%1$d - %2$s", item.getTeamNumber(), item.getTeam().getName()));
        holder.pits.setText(String.valueOf(item.getDetails().getPitStops()));

        Session session = item.getSession();
        holder.driverTimeView.setSession(session);
        holder.sessionTimeView.setSession(session);
        if (session != null) {
            Car car = session.getCar();
            holder.btnSessionCar.setText(car == null ? context.getString(R.string.btn_set_car) : String.valueOf(car.getNumber()));

            Driver driver = session.getDriver();
            holder.btnSessionDriver.setText(driver == null ? context.getString(R.string.btn_set_driver) : session.getDriver().getName());
            holder.driverTimeView.setVisibility(driver == null ? View.GONE : View.VISIBLE);
            if (driver != null) {
                holder.driverTimeView.setPrevDuration(item.getDetails().getDriverDuration(driver.getId()));
            }

            holder.sessionType.setText(session.getType().getTitle());
            holder.sessionType.setVisibility(View.VISIBLE);

            holder.btnNextSession.setVisibility(isPitStopsRemoved ? View.VISIBLE : View.GONE);
            holder.btnPitOut.setVisibility(isPitStopsRemoved ? View.GONE : View.VISIBLE);
            switch (session.getType()) {
                case TRACK:
                    holder.sessionType.setTextColor(Color.GREEN);
                    holder.sessionTimeView.setTextColor(Color.GREEN);
                    holder.btnPitOut.setChecked(false);
                    break;

                case PIT:
                    holder.sessionType.setTextColor(Color.RED);
                    holder.sessionTimeView.setTextColor(Color.RED);
                    holder.btnPitOut.setChecked(true);
                    break;

                default:
                    holder.btnNextSession.setVisibility(View.GONE);
                    holder.btnPitOut.setVisibility(View.GONE);
                    break;
            }
        }

        holder.btnSessionCar.setOnClickListener(v -> {
            if (onSetCarClickListener != null) onSetCarClickListener.onItemClick(item, position);
        });

        holder.btnSessionDriver.setOnClickListener(v -> {
            if (onSetDriverClickListener != null) onSetDriverClickListener.onItemClick(item, position);
        });

        RxView.clicks(holder.btnNextSession).subscribe(aVoid -> {
            if (onOutClickListener != null) onOutClickListener.onItemClick(item, position);
        });

        RxView.clicks(holder.btnPitOut).subscribe(v -> {
            if (holder.btnPitOut.isChecked()) {
                if (onPitClickListener != null) onPitClickListener.onItemClick(item, position);
            } else {
                if (onOutClickListener != null) onOutClickListener.onItemClick(item, position);
            }
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
        // Car
        private Button btnSessionCar;

        // Driver
        private DriverTimeView driverTimeView;
        private Button btnSessionDriver;

        private TextView team, sessionType, pits;
        private SessionTimeView sessionTimeView;
        private ToggleButton btnPitOut;

        private Button btnNextSession;

        public MyHolder(View itemView) {
            super(itemView);

            btnSessionCar = UiUtils.findView(itemView, R.id.btn_session_car);

            btnSessionDriver = UiUtils.findView(itemView, R.id.btn_session_driver);
            driverTimeView = UiUtils.findView(itemView, R.id.tv_driver_all_time);

            team = UiUtils.findView(itemView, R.id.tv_team);
            sessionTimeView = UiUtils.findView(itemView, R.id.tv_session_duration);
            sessionType = UiUtils.findView(itemView, R.id.tv_session_type);

            pits = UiUtils.findView(itemView, R.id.tv_pits);

            btnNextSession = UiUtils.findView(itemView, R.id.btn_next);


            btnPitOut = UiUtils.findView(itemView, R.id.btn_pit_out);
        }
    }
}