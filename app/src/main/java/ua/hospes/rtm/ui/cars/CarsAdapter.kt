package ua.hospes.rtm.ui.cars

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_car.view.*
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.cars.Car

class CarsAdapter : ListAdapter<Car, CarsAdapter.MyHolder>(DIFF_CALLBACK) {
    var itemClickListener: ((car: Car) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder =
            MyHolder(parent) { itemClickListener?.invoke(getItem(it)) }

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(getItem(position))


    inner class MyHolder(parent: ViewGroup, itemClickListener: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)) {

        init {
            itemView.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                itemClickListener.invoke(adapterPosition)
            }
        }

        fun bind(item: Car) = with(itemView) {
            number.text = item.number.toString()
            when (item.broken) {
                true -> {
                    number.setTextColor(Color.GRAY)
                    broken.visibility = View.VISIBLE
                }
                else -> {
                    number.setTextColor(resources.getColor(item.quality.color))
                    broken.visibility = View.GONE
                }
            }
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Car>() {
    override fun areItemsTheSame(old: Car, new: Car): Boolean = old.id == new.id

    override fun areContentsTheSame(old: Car, new: Car): Boolean = old == new
}