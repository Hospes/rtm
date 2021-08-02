package ua.hospes.rtm.ui.race.detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.ItemRaceDetailSessionBinding
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.TimeUtils

class SessionAdapter : ListAdapter<Session, SessionAdapter.MyHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(parent)

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position), position)

    inner class MyHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_race_detail_session, parent, false)) {

        private val binding = ItemRaceDetailSessionBinding.bind(itemView)

        fun bind(item: Session, position: Int) = with(itemView) {
            val bg1 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
            val bg2 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

            setBackgroundResource(if (position % 2 == 0) bg1.data else bg2.data);


            binding.car.text = item.car?.number?.toString() ?: ""
            if (item.raceStartTime == null || item.startTime == null) {
                binding.start.text = ""
            } else {
                binding.start.text = TimeUtils.formatNanoWithMills(item.startTime - item.raceStartTime);
            }

            binding.driver.text = item.driver?.name ?: ""
            if (item.startTime == null || item.endTime == null)
                binding.duration.setText(R.string.race_now)
            else
                binding.duration.text = TimeUtils.formatNanoWithMills(item.endTime - item.startTime)

            binding.type.text = when (item.type) {
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