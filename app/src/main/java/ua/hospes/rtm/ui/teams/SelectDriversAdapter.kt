package ua.hospes.rtm.ui.teams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_driver_selectable.view.*
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.widgets.CheckableFrameLayout

class SelectDriversAdapter : ListAdapter<Driver, SelectDriversAdapter.MyHolder>(DIFF_CALLBACK) {
    private val selectedIds = hashSetOf<Long>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(parent)
    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position))

    fun getSelectedIds() = selectedIds.toList()

    fun setSelected(list: List<Driver>) {
        list.forEach { driver -> driver.id.let { selectedIds.add(it) } }
        notifyDataSetChanged()
    }


    inner class MyHolder(parent: ViewGroup, @LayoutRes layoutId: Int = R.layout.item_driver_selectable)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

                val item = getItem(bindingAdapterPosition)
                when (selectedIds.contains(item.id)) {
                    true -> selectedIds.remove(item.id)
                    else -> item.id.let { selectedIds.add(it) }
                }
                notifyItemChanged(bindingAdapterPosition)
            }
        }

        fun bind(item: Driver) = with(itemView as CheckableFrameLayout) {
            cb.text = when (item.teamId) {
                null -> item.name
                else -> "${item.name} (${item.teamName})"
            }
            isChecked = selectedIds.contains(item.id)
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Driver>() {
    override fun areItemsTheSame(old: Driver, new: Driver): Boolean = old.id == new.id

    override fun areContentsTheSame(old: Driver, new: Driver): Boolean = old == new
}