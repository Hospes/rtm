package ua.hospes.rtm.core.db.tables;

import ua.hospes.dbhelper.AbsDbTable;

/**
 * @author Andrew Khloponin
 */
public class Teams extends AbsDbTable {
    public static final String name = "Teams";

    public static final String NAME = "name";


    @Override
    public String create() {
        return CREATE_TABLE + name + " (" +
                NAME + TEXT_TYPE +
                " );";
    }

    @Override
    public String drop() {
        return DROP_TABLE_IF_EXISTS + name;
    }
}