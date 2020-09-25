package ua.hospes.rtm.ui.race.detail

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.ItemDriverDetailsBinding
import ua.hospes.rtm.domain.race.models.DriverDetails

class DriverDetailsAdapter : ListAdapter<DriverDetails, DriverDetailsAdapter.MyHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(parent)

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position), position)

    inner class MyHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_driver_details, parent, false)) {

        private val binding = ItemDriverDetailsBinding.bind(itemView)

        fun bind(item: DriverDetails, position: Int) = with(itemView) {
            val bg1 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
            val bg2 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

            val resBG = if ((position / 2) % 2 == 0) {
                if (position % 2 == 0) bg1.data else bg2.data
            } else {
                if (position % 2 == 0) bg2.data else bg1.data
            }
            setBackgroundResource(resBG)

            binding.name.text = item.name
            binding.driver.session = item.session
            binding.driver.prevDuration = item.prevDuration
        }
    }

}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DriverDetails>() {
    override fun areItemsTheSame(old: DriverDetails, new: DriverDetails): Boolean = old.id == new.id

    override fun areContentsTheSame(old: DriverDetails, new: DriverDetails): Boolean = old == new
}