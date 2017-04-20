package ua.hospes.rtm.domain.race.models;

import android.util.SparseArray;

/**
 * @author Andrew Khloponin
 */
public class RaceItemDetails {
    private int pitStops = 0;
    private SparseArray<Long> completedDriversDuration = new SparseArray<>();


    public RaceItemDetails() {}


    public void addDriverDuration(int driverId, long duration) {
        long oldDuration = getDriverDuration(driverId);
        completedDriversDuration.put(driverId, oldDuration + duration);
    }

    public long getDriverDuration(int driverId) {
        Long l = completedDriversDuration.get(driverId);
        return l == null ? 0L : l;
    }


    public int getPitStops() {
        return pitStops;
    }

    public void setPitStops(int pitStops) {
        this.pitStops = pitStops;
    }


    @Override
    public String toString() {
        return "RaceItemDetails{" +
                "pitStops=" + pitStops +
                ", completedDriversDuration=" + completedDriversDuration +
                '}';
    }
}