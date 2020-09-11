package ua.hospes.rtm.ui.drivers

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_driver.view.*
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.drivers.Driver

class DriversAdapter : ListAdapter<Driver, DriversAdapter.MyHolder>(DIFF_CALLBACK) {
    var itemClickListener: ((item: Driver) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder =
            MyHolder(parent, itemClickListener = { itemClickListener?.invoke(getItem(it)) })

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position), position)


    inner class MyHolder(parent: ViewGroup, @LayoutRes layoutId: Int = R.layout.item_driver, itemClickListener: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                itemClickListener.invoke(bindingAdapterPosition)
            }
        }

        fun bind(item: Driver, position: Int) = with(itemView) {
            val bg1 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground1, bg1, false)
            val bg2 = TypedValue()
            context.theme.resolveAttribute(R.attr.listItemBackground2, bg2, false)

            setBackgroundResource(if (position % 2 == 0) bg2.data else bg1.data)

            name.text = item.name
            team.text = item.teamName ?: "No team"
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Driver>() {
    override fun areItemsTheSame(old: Driver, new: Driver): Boolean = old.id == new.id

    override fun areContentsTheSame(old: Driver, new: Driver): Boolean = old == new
}