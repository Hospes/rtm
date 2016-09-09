package ua.hospes.nfs.marathon.core.db.tables;

import android.provider.BaseColumns;

/**
 * @author Andrew Khloponin
 */
public interface Sessions extends BaseColumns {
    String name = "Sessions";

    String TEAM_ID = "team_id";
    String DRIVER_ID = "driver_id";
    String CAR_ID = "car_id";
    String START_DURATION_TIME = "start_duration_time";
    String END_DURATION_TIME = "end_duration_time";
    String TYPE = "type";
}