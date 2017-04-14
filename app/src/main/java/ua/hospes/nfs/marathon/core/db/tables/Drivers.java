package ua.hospes.nfs.marathon.core.db.tables;

import ua.hospes.dbhelper.AbsDbTable;

/**
 * @author Andrew Khloponin
 */
public class Drivers extends AbsDbTable {
    public static final String name = "Drivers";

    public static final String NAME = "name";
    public static final String TEAM_ID = "team_id";


    @Override
    public String create() {
        return CREATE_TABLE + name + " (" +
                NAME + TEXT_TYPE + COMMA_SEP +
                TEAM_ID + INTEGER_TYPE +
                " );";
    }

    @Override
    public String drop() {
        return DROP_TABLE_IF_EXISTS + name;
    }
}