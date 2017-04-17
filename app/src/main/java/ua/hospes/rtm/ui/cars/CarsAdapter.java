package ua.hospes.rtm.ui.cars;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.hospes.absrvadapter.AbsRecyclerAdapter;
import ua.hospes.absrvadapter.AbsRecyclerHolder;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class CarsAdapter extends AbsRecyclerAdapter<Car, CarsAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent, R.layout.item_car);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, Car item, int position) {
        Resources res = holder.itemView.getResources();
        holder.number.setText(String.valueOf(item.getNumber()));
        holder.number.setTextColor(item.isBroken() ? Color.GRAY : res.getColor(item.getQuality().getColor()));
        holder.broken.setVisibility(item.isBroken() ? View.VISIBLE : View.GONE);
    }


    public class MyHolder extends AbsRecyclerHolder {
        private TextView number, broken;

        MyHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);

            itemView.setOnClickListener(this::initClickListener);
        }


        @Override
        protected void findViews(View itemView) {
            number = UiUtils.findView(itemView, R.id.number);
            broken = UiUtils.findView(itemView, R.id.broken);
        }

        void initClickListener(View view) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (getOnItemClickListener() == null) return;
            onItemClick(getItem(position), position);
        }
    }
}