package ua.hospes.nfs.marathon.core.db.tables;

import android.provider.BaseColumns;

/**
 * @author Andrew Khloponin
 */
public interface Race extends BaseColumns {
    String name = "Race";

    String TEAM_ID = "team_id";
    String TEAM_NUMBER = "team_number";
    String SESSION_ID = "session_id";
    String ORDER = "order_val";
}