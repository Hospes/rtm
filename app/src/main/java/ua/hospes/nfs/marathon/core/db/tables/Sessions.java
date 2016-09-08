package ua.hospes.nfs.marathon.core.db.tables;

import android.provider.BaseColumns;

/**
 * @author Andrew Khloponin
 */
public interface Sessions extends BaseColumns {
    String name = "Sessions";

    String TEAM_ID = "team_id";
    String RIDER_ID = "rider_id";
    String NAME = "name";
}