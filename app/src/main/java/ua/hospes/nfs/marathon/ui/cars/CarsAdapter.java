package ua.hospes.nfs.marathon.ui.cars;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.adapter.AbsRecyclerAdapter;
import ua.hospes.nfs.marathon.domain.cars.models.Car;
import ua.hospes.nfs.marathon.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
public class CarsAdapter extends AbsRecyclerAdapter<Car, CarsAdapter.MyHolder> {
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Car item = getItem(position);

        holder.id.setText(String.valueOf(item.getId()));
        holder.number.setText(String.valueOf(item.getNumber()));
        holder.rating.setText(String.format(Locale.getDefault(), "%1$d / 10", item.getRating()));

        holder.edit.setOnClickListener(view -> getOnItemClickListener().onItemClick(item, position));
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView id, number, rating;
        private Button edit;

        public MyHolder(View itemView) {
            super(itemView);

            id = UiUtils.findView(itemView, R.id.id);
            number = UiUtils.findView(itemView, R.id.number);
            rating = UiUtils.findView(itemView, R.id.rating);
            edit = UiUtils.findView(itemView, R.id.edit);
        }
    }
}