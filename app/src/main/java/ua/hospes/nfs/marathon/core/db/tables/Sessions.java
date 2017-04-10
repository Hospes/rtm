package ua.hospes.nfs.marathon.core.db.tables;

import ua.hospes.dbhelper.AbsDbTable;

/**
 * @author Andrew Khloponin
 */
public class Sessions extends AbsDbTable {
    public static final String name = "Sessions";

    public static final String TEAM_ID = "team_id";
    public static final String DRIVER_ID = "driver_id";
    public static final String CAR_ID = "car_id";
    public static final String START_DURATION_TIME = "start_duration_time";
    public static final String END_DURATION_TIME = "end_duration_time";
    public static final String TYPE = "type";


    @Override
    public String create() {
        return CREATE_TABLE + name + " (" +
                _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                TEAM_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                DRIVER_ID + INTEGER_TYPE + COMMA_SEP +
                CAR_ID + INTEGER_TYPE + COMMA_SEP +
                START_DURATION_TIME + INTEGER_TYPE + DEFAULT + " -1 " + COMMA_SEP +
                END_DURATION_TIME + INTEGER_TYPE + DEFAULT + " -1 " + COMMA_SEP +
                TYPE + TEXT_TYPE + NOT_NULL +
                " );";
    }

    @Override
    public String drop() {
        return DROP_TABLE_IF_EXISTS + name;
    }
}