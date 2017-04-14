package ua.hospes.nfs.marathon.core.db.tables;

import ua.hospes.dbhelper.AbsDbTable;

/**
 * @author Andrew Khloponin
 */
public class Cars extends AbsDbTable {
    public static final String name = "Cars";

    public static final String NUMBER = "numb";
    public static final String RATING = "rating";


    @Override
    public String create() {
        return CREATE_TABLE + name + " (" +
                NUMBER + INTEGER_TYPE + DEFAULT + " 0 " + COMMA_SEP +
                RATING + INTEGER_TYPE + DEFAULT + " 0 " +
                " );";
    }

    @Override
    public String drop() {
        return DROP_TABLE_IF_EXISTS + name;
    }
}