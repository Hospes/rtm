package ua.hospes.rtm.ui.race.detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.race.models.DriverDetails

class DriverDetailsAdapter : ListAdapter<DriverDetails, DriverDetailsAdapter.MyHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(parent)

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position), position)

    inner class MyHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_driver_details, parent, false)) {

        fun bind(item: DriverDetails, position: Int) = with(itemView) {
            val bg1 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
            val bg2 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

            setBackgroundResource(if (position % 2 == 0) bg1.data else bg2.data);

            //        int resBG;
            //        if ((position / 2) % 2 == 0) {
            //            resBG = position % 2 == 0 ? bg1.data : bg2.data;
            //        } else {
            //            resBG = position % 2 == 0 ? bg2.data : bg1.data;
            //        }
            //        holder.itemView.setBackgroundResource(resBG);
            //
            //        holder.name.setText(item.getName());
            //        holder.driver.setSession(item.getSession());
            //        holder.driver.setPrevDuration(item.getPrevDuration());
        }
    }

}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DriverDetails>() {
    override fun areItemsTheSame(old: DriverDetails, new: DriverDetails): Boolean = old.id == new.id

    override fun areContentsTheSame(old: DriverDetails, new: DriverDetails): Boolean = old.equals(new)
}