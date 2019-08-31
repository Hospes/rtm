package ua.hospes.rtm.ui.race.detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.sessions.Session

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
            //
            //
            //        Car car = item.getCar();
            //        holder.car.setText(car == null ? "" : String.valueOf(car.getNumber()));
            //        if (item.getRaceStartTime() == -1 || item.getStartDurationTime() == -1) {
            //            holder.start.setText("");
            //        } else {
            //            holder.start.setText(TimeUtils.formatNanoWithMills(item.getStartDurationTime() - item.getRaceStartTime()));
            //        }
            //        holder.driver.setText(item.getDriver() == null ? "" : item.getDriver().getName());
            //        if (item.getStartDurationTime() == -1 || item.getEndDurationTime() == -1)
            //            holder.duration.setText(R.string.race_now);
            //        else
            //            holder.duration.setText(TimeUtils.formatNanoWithMills(item.getEndDurationTime() - item.getStartDurationTime()));
            //        holder.type.setText(item.getType().getTitle());
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(old: Session, new: Session): Boolean = old.id == new.id

    override fun areContentsTheSame(old: Session, new: Session): Boolean = old.equals(new)
}