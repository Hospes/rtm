package ua.hospes.rtm.ui.race.detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_race_detail_session.view.*
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.TimeUtils

class SessionAdapter : ListAdapter<Session, SessionAdapter.MyHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(parent)

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position), position)

    inner class MyHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_race_detail_session, parent, false)) {

        fun bind(item: Session, position: Int) = with(itemView) {
            val bg1 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
            val bg2 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

            setBackgroundResource(if (position % 2 == 0) bg1.data else bg2.data);


            car.text = item.car?.number?.toString() ?: ""
            if (item.raceStartTime == null || item.startDurationTime == null) {
                start.text = ""
            } else {
                start.text = TimeUtils.formatNanoWithMills(item.startDurationTime - item.raceStartTime);
            }

            driver.text = item.driver?.name ?: ""
            if (item.startDurationTime == null || item.endDurationTime == null)
                duration.setText(R.string.race_now)
            else
                duration.text = TimeUtils.formatNanoWithMills(item.endDurationTime - item.startDurationTime)

            type.text = when (item.type) {
                Session.Type.PIT -> "PIT"
                Session.Type.TRACK -> "TRACK"
            }
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(old: Session, new: Session): Boolean = old.id == new.id

    override fun areContentsTheSame(old: Session, new: Session): Boolean = old == new
}