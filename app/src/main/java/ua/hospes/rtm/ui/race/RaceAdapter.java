package ua.hospes.rtm.ui.race;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Locale;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.absrvadapter.OnItemClickListener;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.preferences.PreferencesManager;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.ui.race.widgets.DriverTimeView;
import ua.hospes.rtm.ui.race.widgets.SessionTimeView;
import ua.hospes.rtm.utils.UiUtils;
import ua.hospes.undobutton.UndoButton;
import ua.hospes.undobutton.UndoButtonController;

/**
 * @author Andrew Khloponin
 */
class RaceAdapter extends AbsRecyclerAdapter<RaceItem, RaceAdapter.MyHolder> {
    private final UndoButtonController undoButtonController;
    private final boolean isPitStopsRemoved;
    @ColorInt private final int carDefaultColor, sessionTrackColor, sessionPitColor;
    private OnItemClickListener<RaceItem> onSetCarClickListener;
    private OnItemClickListener<RaceItem> onSetDriverClickListener;
    private OnItemClickListener<RaceItem> onPitClickListener;
    private OnItemClickListener<RaceItem> onOutClickListener;
    private OnItemClickListener<RaceItem> onUndoClickListener;


    RaceAdapter(Context context, @NonNull PreferencesManager preferencesManager, UndoButtonController undoButtonController) {
        this.isPitStopsRemoved = preferencesManager.isPitStopSessionsRemoved();
        this.undoButtonController = undoButtonController;
        this.carDefaultColor = getButtonTextAppearance(context);
        this.sessionTrackColor = getSessionTrackColor(context);
        this.sessionPitColor = getSessionPitColor(context);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_race);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, RaceItem item, int position) {
        Context context = holder.itemView.getContext();
        holder.itemView.setBackgroundResource(position % 2 == 0 ? R.drawable.bg_list_item_1 : R.drawable.bg_list_item_2);

        holder.team.setText(String.format(Locale.getDefault(), "%1$d - %2$s", item.getTeamNumber(), item.getTeam().getName()));
        holder.pits.setText(context.getResources().getString(R.string.race_pit, item.getDetails().getPitStops()));

        Session session = item.getSession();
        holder.driverTimeView.setSession(session);
        holder.sessionTimeView.setSession(session);

        undoButtonController.onBind(item.getId(), holder.btnNextSession);

        if (session != null) {
            Car car = session.getCar();
            holder.btnSessionCar.setText(car == null ? context.getString(R.string.race_btn_set_car) : String.valueOf(car.getNumber()));
            holder.btnSessionCar.setTextColor(car == null ? carDefaultColor : context.getResources().getColor(car.getQuality().getColor()));

            Driver driver = session.getDriver();
            holder.btnSessionDriver.setText(driver == null ? context.getString(R.string.race_btn_set_driver) : session.getDriver().getName());
            holder.driverTimeView.setVisibility(driver == null ? View.INVISIBLE : View.VISIBLE);
            if (driver != null) {
                holder.driverTimeView.setPrevDuration(item.getDetails().getDriverDuration(driver.getId()));
            }

            holder.sessionType.setText(session.getType().getTitle());
            holder.sessionType.setVisibility(View.VISIBLE);

            holder.btnNextSession.setVisibility(isPitStopsRemoved ? View.VISIBLE : View.GONE);

            holder.btnPitOut.setVisibility(isPitStopsRemoved ? View.GONE : View.VISIBLE);
            switch (session.getType()) {
                case TRACK:
                    holder.sessionType.setTextColor(sessionTrackColor);
                    holder.sessionTimeView.setTextColor(sessionTrackColor);
                    holder.btnPitOut.setChecked(false);
                    break;

                case PIT:
                    holder.sessionType.setTextColor(sessionPitColor);
                    holder.sessionTimeView.setTextColor(sessionPitColor);
                    holder.btnPitOut.setChecked(true);
                    break;

                default:
                    holder.btnNextSession.setVisibility(View.GONE);
                    holder.btnPitOut.setVisibility(View.GONE);
                    break;
            }
        }
    }


    void setOnSetCarClickListener(OnItemClickListener<RaceItem> onSetCarClickListener) {
        this.onSetCarClickListener = onSetCarClickListener;
    }

    void setOnSetDriverClickListener(OnItemClickListener<RaceItem> onSetDriverClickListener) {
        this.onSetDriverClickListener = onSetDriverClickListener;
    }

    void setOnPitClickListener(OnItemClickListener<RaceItem> onPitClickListener) {
        this.onPitClickListener = onPitClickListener;
    }

    void setOnOutClickListener(OnItemClickListener<RaceItem> onOutClickListener) {
        this.onOutClickListener = onOutClickListener;
    }

    void setOnUndoClickListener(OnItemClickListener<RaceItem> onUndoClickListener) {
        this.onUndoClickListener = onUndoClickListener;
    }


    class MyHolder extends AbsRecyclerHolder {
        // Car
        Button btnSessionCar;

        // Driver
        DriverTimeView driverTimeView;
        Button btnSessionDriver;

        TextView team, sessionType, pits;
        SessionTimeView sessionTimeView;
        ToggleButton btnPitOut;

        UndoButton btnNextSession;


        MyHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);

            itemView.setOnClickListener(this::initOnItemClickListener);

            btnSessionCar.setOnClickListener(this::initOnSetCarClickListener);
            btnSessionDriver.setOnClickListener(this::initOnSetDriverClickListener);

            btnNextSession.setOnClickListener(this::initOnOutClickListener);
            btnNextSession.setOnUndoClickListener(this::initOnUndoClickListener);
            btnNextSession.setController(undoButtonController);
            btnPitOut.setOnClickListener(this::initOnPitClickListener);
        }


        @Override
        protected void findViews(View itemView) {
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

        void initOnItemClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (getOnItemClickListener() == null) return;
            onItemClick(getItem(position), position);
        }

        void initOnSetCarClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (onSetCarClickListener == null) return;
            onSetCarClickListener.onItemClick(getItem(position), position);
        }

        void initOnSetDriverClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (onSetDriverClickListener == null) return;
            onSetDriverClickListener.onItemClick(getItem(position), position);
        }

        void initOnPitClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (btnPitOut.isChecked()) {
                if (onPitClickListener != null) onPitClickListener.onItemClick(getItem(position), position);
            } else {
                if (onOutClickListener != null) onOutClickListener.onItemClick(getItem(position), position);
            }
        }

        void initOnOutClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (onOutClickListener == null) return;
            onOutClickListener.onItemClick(getItem(position), position);
        }

        void initOnUndoClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (onUndoClickListener == null) return;
            onUndoClickListener.onItemClick(getItem(position), position);
        }
    }


    @ColorInt
    private int getButtonTextAppearance(Context context) {
        TypedArray ta = null;
        try {
            int[] attrs = {android.R.attr.textColorPrimary};
            ta = context.obtainStyledAttributes(attrs);
            return ta.getColor(0, Color.WHITE);
        } finally {
            if (ta != null) ta.recycle();
        }
    }

    @ColorInt
    private int getSessionTrackColor(Context context) {
        TypedArray ta = null;
        try {
            int[] attrs = {R.attr.timeView_OnTrack};
            ta = context.obtainStyledAttributes(attrs);
            return ta.getColor(0, Color.GREEN);
        } finally {
            if (ta != null) ta.recycle();
        }
    }

    @ColorInt
    private int getSessionPitColor(Context context) {
        TypedArray ta = null;
        try {
            int[] attrs = {R.attr.timeView_OnPit};
            ta = context.obtainStyledAttributes(attrs);
            return ta.getColor(0, Color.RED);
        } finally {
            if (ta != null) ta.recycle();
        }
    }
}