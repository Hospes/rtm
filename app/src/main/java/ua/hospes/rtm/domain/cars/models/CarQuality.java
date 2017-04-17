package ua.hospes.rtm.domain.cars.models;

import android.support.annotation.ColorRes;

import ua.hospes.rtm.R;

/**
 * @author Andrew Khloponin
 */
public enum CarQuality {
    LOW(R.color.car_quality_low),
    NORMAL(R.color.car_quality_normal),
    HIGH(R.color.car_quality_high);


    @ColorRes private int color;

    CarQuality(@ColorRes int color) {
        this.color = color;
    }

    @ColorRes
    public int getColor() {
        return color;
    }


    public static CarQuality fromString(String s) {
        for (CarQuality quality : CarQuality.values())
            if (quality.toString().equalsIgnoreCase(s)) return quality;
        return NORMAL;
    }
}