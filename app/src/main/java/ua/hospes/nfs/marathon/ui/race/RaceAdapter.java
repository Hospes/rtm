package ua.hospes.nfs.marathon.ui.race;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.adapter.AbsRecyclerAdapter;
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
        RaceItem item = getItem(position);

        holder.team.setText(String.format(Locale.getDefault(), "%1$d - %2$s", item.getTeamNumber(), item.getTeam().getName()));

        Session session = item.getSession();
        holder.driverTimeView.setPrevDuration(item.getDriverPrevDuration());
        holder.driverTimeView.setSession(session);
        holder.sessionTimeView.setSession(session);
        if (session != null) {
            Driver driver = session.getDriver();
            if (driver != null) {
                holder.btnSetDriver.setVisibility(View.GONE);
                holder.cSessionDriver.setVisibility(View.VISIBLE);

                holder.driver.setText(session.getDriver().getName());
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

        holder.btnSetDriver.setOnClickListener(v -> {
            if (onSetDriverClickListener != null) onSetDriverClickListener.onItemClick(item, position);
        });

        holder.pit.setOnClickListener(v -> {
            if (onPitClickListener != null) onPitClickListener.onItemClick(item, position);
        });

        holder.out.setOnClickListener(v -> {
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
        private View cSessionDriver;
        private DriverTimeView driverTimeView;
        private SessionTimeView sessionTimeView;
        private TextView driver, team, sessionType;
        private Button pit, out, btnSetDriver;

        public MyHolder(View itemView) {
            super(itemView);

            team = UiUtils.findView(itemView, R.id.tv_team);
            sessionTimeView = UiUtils.findView(itemView, R.id.tv_session_duration);
            driver = UiUtils.findView(itemView, R.id.tv_session_driver);
            sessionType = UiUtils.findView(itemView, R.id.tv_session_type);

            driverTimeView = UiUtils.findView(itemView, R.id.tv_driver_all_time);

            btnSetDriver = UiUtils.findView(itemView, R.id.btn_set_driver);

            cSessionDriver = UiUtils.findView(itemView, R.id.container_session_driver);

            pit = UiUtils.findView(itemView, R.id.btn_pit);
            out = UiUtils.findView(itemView, R.id.btn_out);
        }
    }
}