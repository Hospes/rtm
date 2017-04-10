package ua.hospes.nfs.marathon.core.db.tables;

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
                _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                NAME + TEXT_TYPE +
                " );";
    }

    @Override
    public String drop() {
        return DROP_TABLE_IF_EXISTS + name;
    }
}