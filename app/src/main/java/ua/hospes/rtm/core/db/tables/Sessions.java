package ua.hospes.rtm.core.db.tables;

import java.util.Arrays;
import java.util.List;

import ua.hospes.dbhelper.builder.CreateQuery;
import ua.hospes.dbhelper.builder.DropQuery;
import ua.hospes.dbhelper.builder.models.Column;
import ua.hospes.dbhelper.builder.models.Constraint;
import ua.hospes.dbhelper.builder.models.DataType;
import ua.hospes.dbhelper.builder.models.DbColumn;

/**
 * @author Andrew Khloponin
 */
@SuppressWarnings("unchecked")
public interface Sessions {
    String name = "Sessions";
    Column<Integer> ID = DbColumn.ID();
    Column<Integer> TEAM_ID = DbColumn.newInstance("team_id", DataType.INTEGER, Constraint.NOT_NULL());
    Column<Integer> DRIVER_ID = DbColumn.newInstance("driver_id", DataType.INTEGER);
    Column<Integer> CAR_ID = DbColumn.newInstance("car_id", DataType.INTEGER);
    Column<Integer> RACE_START_TIME = DbColumn.newInstance("race_start_time", DataType.INTEGER, Constraint.DEFAULT(-1));
    Column<Integer> START_DURATION_TIME = DbColumn.newInstance("start_duration_time", DataType.INTEGER, Constraint.DEFAULT(-1));
    Column<Integer> END_DURATION_TIME = DbColumn.newInstance("end_duration_time", DataType.INTEGER, Constraint.DEFAULT(-1));
    Column<String> TYPE = DbColumn.newInstance("type", DataType.TEXT, Constraint.NOT_NULL());


    static List<Column> columns() {
        return Arrays.asList(ID, TEAM_ID, DRIVER_ID, CAR_ID, RACE_START_TIME, START_DURATION_TIME, END_DURATION_TIME, TYPE);
    }

    static CreateQuery create() {
        return new CreateQuery().tableName(name).columns(columns()).ifNotExists(true);
    }

    static DropQuery drop() {
        return new DropQuery().tableName(name).ifExists(true);
    }
}