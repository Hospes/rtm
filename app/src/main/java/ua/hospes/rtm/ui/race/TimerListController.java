package ua.hospes.rtm.ui.race;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ua.hospes.rtm.core.ui.RxScrollListener;
import ua.hospes.rtm.widgets.TimeView;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
class TimerListController extends RxScrollListener {
    private static final float AUTO_PLAY_AREA_START_PADDING_RELATIVE = 0.1f;
    private static final float AUTO_PLAY_AREA_END_PADDING_RELATIVE = 0.1f;

    private Set<TimeView> playingItems = new HashSet<>();
    private static final long SKIP_RECALCULATION_DURATION = 300;

    private long lastRecalculationTime;


    TimerListController() {
        RxUtils.manage(this, subject.debounce(SKIP_RECALCULATION_DURATION, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedulers())
                .subscribe(this::onActionCall));
    }

    private void onActionCall(RecyclerView recyclerView) {
        if (System.currentTimeMillis() < lastRecalculationTime + SKIP_RECALCULATION_DURATION) return;

        lastRecalculationTime = System.currentTimeMillis();

        playingItems = collectTimeViews(recyclerView);
    }

    @SuppressWarnings("ConstantConditions")
    private Set<TimeView> collectTimeViews(RecyclerView rv) {
        Set<TimeView> set = new HashSet<>();

        RecyclerView.LayoutManager lm = rv.getLayoutManager();

        int autoPlayAreaStart = (int) (rv.getTop() - rv.getHeight() * AUTO_PLAY_AREA_START_PADDING_RELATIVE);
        int autoPlayAreaEnd = (int) (rv.getBottom() + rv.getHeight() * AUTO_PLAY_AREA_END_PADDING_RELATIVE);

        int count = lm.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = lm.getChildAt(i);
            int viewStart = lm.getDecoratedTop(child);
            int viewEnd = lm.getDecoratedBottom(child);

            boolean shouldPlay = false;
            shouldPlay = shouldPlay || (rv.getTop() <= viewStart && rv.getBottom() >= viewEnd); // completely visible
            shouldPlay = shouldPlay || !(autoPlayAreaStart > viewEnd || autoPlayAreaEnd < viewStart); // near center;

            if (shouldPlay) {
                RaceAdapter.MyHolder viewHolder = (RaceAdapter.MyHolder) rv.getChildViewHolder(child);
                set.add(viewHolder.sessionTimeView);
                set.add(viewHolder.driverTimeView);
            }
        }
        return set;
    }


    public void updateTime(long currentNanoTime) {
        for (TimeView tv : playingItems) tv.setCurrentNanoTime(currentNanoTime);
    }

    public void unsubscribe() {
        RxUtils.unsubscribe(this);
    }
}