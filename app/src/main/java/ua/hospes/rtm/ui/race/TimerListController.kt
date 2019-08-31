package ua.hospes.rtm.ui.race

import android.view.View

import androidx.recyclerview.widget.RecyclerView

import java.util.HashSet
import java.util.concurrent.TimeUnit

import ua.hospes.rtm.core.ui.RxScrollListener
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.widgets.TimeView

internal class TimerListController : RxScrollListener() {

    private var playingItems: Set<TimeView> = HashSet()

    private var lastRecalculationTime: Long = 0

    init {
        RxUtils.manage(this, subject.debounce(SKIP_RECALCULATION_DURATION, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedulers())
                .subscribe(Consumer<RecyclerView> { this.onActionCall(it) }))
    }

    private fun onActionCall(recyclerView: RecyclerView) {
        if (System.currentTimeMillis() < lastRecalculationTime + SKIP_RECALCULATION_DURATION) return

        lastRecalculationTime = System.currentTimeMillis()

        playingItems = collectTimeViews(recyclerView)
    }

    private fun collectTimeViews(rv: RecyclerView): Set<TimeView> {
        val set = HashSet<TimeView>()

        val lm = rv.layoutManager

        val autoPlayAreaStart = (rv.top - rv.height * AUTO_PLAY_AREA_START_PADDING_RELATIVE).toInt()
        val autoPlayAreaEnd = (rv.bottom + rv.height * AUTO_PLAY_AREA_END_PADDING_RELATIVE).toInt()

        val count = lm!!.childCount
        for (i in 0 until count) {
            val child = lm.getChildAt(i)
            val viewStart = lm.getDecoratedTop(child!!)
            val viewEnd = lm.getDecoratedBottom(child)

            var shouldPlay = false
            shouldPlay = shouldPlay || rv.top <= viewStart && rv.bottom >= viewEnd // completely visible
            shouldPlay = shouldPlay || !(autoPlayAreaStart > viewEnd || autoPlayAreaEnd < viewStart) // near center;

            if (shouldPlay) {
                val viewHolder = rv.getChildViewHolder(child) as RaceAdapter.MyHolder
                set.add(viewHolder.sessionTimeView)
                set.add(viewHolder.driverTimeView)
            }
        }
        return set
    }


    fun updateTime(currentNanoTime: Long) {
        for (tv in playingItems) tv.currentNanoTime = currentNanoTime
    }

    fun unsubscribe() {
        RxUtils.unsubscribe(this)
    }

    companion object {
        private val AUTO_PLAY_AREA_START_PADDING_RELATIVE = 0.1f
        private val AUTO_PLAY_AREA_END_PADDING_RELATIVE = 0.1f
        private val SKIP_RECALCULATION_DURATION: Long = 300
    }
}