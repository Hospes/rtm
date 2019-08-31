package ua.hospes.rtm.ui.cars

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsSpinnerAdapter
import ua.hospes.rtm.domain.cars.Car

class CarQualityAdapter(ctx: Context, vararg qualities: Car.Quality)
    : AbsSpinnerAdapter<Car.Quality, CarQualityAdapter.MyHolder>(ctx, qualities.toList()) {

    override fun onCreateViewHolder(inflater: LayoutInflater): MyHolder = MyHolder(inflater, R.layout.item_color)

    override fun onBindViewHolder(holder: MyHolder, item: Car.Quality?, position: Int) {
        item?.color?.let { holder.bg.setBackgroundColor(resources.getColor(it)) }
    }


    inner class MyHolder(inflater: LayoutInflater, layoutId: Int) : ViewHolder(inflater, layoutId) {
        val bg: FrameLayout = itemView.findViewById(R.id.root)
    }
}