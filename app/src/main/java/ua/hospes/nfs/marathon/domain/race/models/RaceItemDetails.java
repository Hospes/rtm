package ua.hospes.nfs.marathon.domain.race.models;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Khloponin
 */
public class RaceItemDetails {
    private int pitStops = 0;
    private Map<Integer, Long> completedDriversDuration = new HashMap<>();


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