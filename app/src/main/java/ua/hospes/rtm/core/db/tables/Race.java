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
public interface Race {
    String name = "Race";
    Column<Integer> ID = DbColumn.ID();
    Column<Integer> TEAM_ID = DbColumn.newInstance("team_id", DataType.INTEGER, Constraint.UNIQUE(), Constraint.NOT_NULL());
    Column<Integer> TEAM_NUMBER = DbColumn.newInstance("team_number", DataType.INTEGER, Constraint.DEFAULT(-1));
    Column<Integer> SESSION_ID = DbColumn.newInstance("session_id", DataType.INTEGER);
    Column<Integer> ORDER = DbColumn.newInstance("order_val", DataType.INTEGER);


    static List<Column> columns() {
        return Arrays.asList(ID, TEAM_ID, TEAM_NUMBER, SESSION_ID, ORDER);
    }

    static CreateQuery create() {
        return new CreateQuery().tableName(name).columns(columns()).ifNotExists(true);
    }

    static DropQuery drop() {
        return new DropQuery().tableName(name).ifExists(true);
    }
}