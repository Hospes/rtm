package ua.hospes.rtm.ui.cars;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import ua.hospes.rtm.R;
import ua.hospes.rtm.core.ui.AbsSpinnerAdapter;
import ua.hospes.rtm.domain.cars.models.CarQuality;
import ua.hospes.rtm.utils.UiUtils;

/**
 * @author Andrew Khloponin
 */
class CarQualityAdapter extends AbsSpinnerAdapter<CarQuality, CarQualityAdapter.MyHolder> {
    CarQualityAdapter(@NonNull Context mContext) {
        super(mContext);
    }

    CarQualityAdapter(@NonNull Context mContext, List<CarQuality> objects) {
        super(mContext, objects);
    }

    CarQualityAdapter(@NonNull Context mContext, CarQuality... objects) {
        super(mContext, objects);
    }


    @Override
    protected MyHolder onCreateViewHolder(LayoutInflater inflater) {
        return new MyHolder(inflater, R.layout.item_color);
    }

    @Override
    protected void onBindViewHolder(MyHolder holder, CarQuality item, int position) {
        holder.bg.setBackgroundColor(getResources().getColor(item.getColor()));
    }


    class MyHolder extends AbsSpinnerAdapter.ViewHolder {
        private FrameLayout bg;

        MyHolder(LayoutInflater inflater, int layoutId) {
            super(inflater, layoutId);
        }

        @Override
        protected void findViews(View itemView) {
            bg = UiUtils.findView(itemView, R.id.root);
        }
    }
}