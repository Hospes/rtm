package ua.hospes.rtm.ui.teams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.ItemDriverSelectableBinding
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


    inner class MyHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_driver_selectable, parent, false)) {

        private val binding = ItemDriverSelectableBinding.bind(itemView)

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
            binding.cb.text = when (item.teamId) {
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