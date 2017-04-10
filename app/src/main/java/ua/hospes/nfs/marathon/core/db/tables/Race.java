package ua.hospes.nfs.marathon.core.db.tables;

import ua.hospes.dbhelper.AbsDbTable;

/**
 * @author Andrew Khloponin
 */
public class Race extends AbsDbTable {
    public static final String name = "Race";

    public static final String TEAM_ID = "team_id";
    public static final String TEAM_NUMBER = "team_number";
    public static final String SESSION_ID = "session_id";
    public static final String ORDER = "order_val";


    @Override
    public String create() {
        return CREATE_TABLE + name + " (" +
                _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                TEAM_ID + INTEGER_TYPE + UNIQUE + NOT_NULL + COMMA_SEP +
                TEAM_NUMBER + INTEGER_TYPE + DEFAULT + " -1 " + COMMA_SEP +
                SESSION_ID + INTEGER_TYPE + COMMA_SEP +
                ORDER + INTEGER_TYPE +
                " );";
    }

    @Override
    public String drop() {
        return DROP_TABLE_IF_EXISTS + name;
    }
}