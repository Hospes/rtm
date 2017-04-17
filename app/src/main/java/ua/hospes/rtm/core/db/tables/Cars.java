package ua.hospes.rtm.core.db.tables;

import ua.hospes.dbhelper.AbsDbTable;
import ua.hospes.rtm.domain.cars.models.CarQuality;

/**
 * @author Andrew Khloponin
 */
public class Cars extends AbsDbTable {
    public static final String name = "Cars";

    public static final String NUMBER = "numb";
    public static final String QUALITY = "quality";
    public static final String BROKEN = "broken";


    @Override
    public String create() {
        return CREATE_TABLE + name + " (" +
                NUMBER + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                QUALITY + TEXT_TYPE + NOT_NULL + DEFAULT + " " + CarQuality.NORMAL + " " + COMMA_SEP +
                BROKEN + INTEGER_TYPE + NOT_NULL + DEFAULT + " 0 " +
                " );";
    }

    @Override
    public String drop() {
        return DROP_TABLE_IF_EXISTS + name;
    }
}