package ua.hospes.rtm.ui.cars

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.ItemCarBinding
import ua.hospes.rtm.domain.cars.Car

class CarsAdapter : ListAdapter<Car, CarsAdapter.MyHolder>(DIFF_CALLBACK) {
    var itemClickListener: ((car: Car) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder =
            MyHolder(parent) { itemClickListener?.invoke(getItem(it)) }

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position))


    inner class MyHolder(parent: ViewGroup, itemClickListener: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)) {

        private val binding = ItemCarBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                itemClickListener.invoke(bindingAdapterPosition)
            }
        }

        fun bind(item: Car) = with(itemView) {
            binding.number.text = item.number.toString()
            when (item.broken) {
                true -> {
                    binding.number.setTextColor(Color.GRAY)
                    binding.broken.visibility = View.VISIBLE
                }
                else -> {
                    binding.number.setTextColor(resources.getColor(item.quality.color, itemView.context.theme))
                    binding.broken.visibility = View.GONE
                }
            }
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Car>() {
    override fun areItemsTheSame(old: Car, new: Car): Boolean = old.id == new.id

    override fun areContentsTheSame(old: Car, new: Car): Boolean = old == new
}