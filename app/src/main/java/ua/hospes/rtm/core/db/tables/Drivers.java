package ua.hospes.rtm.core.db.tables;

import java.util.Arrays;
import java.util.List;

import ua.hospes.dbhelper.builder.CreateQuery;
import ua.hospes.dbhelper.builder.DropQuery;
import ua.hospes.dbhelper.builder.models.Column;
import ua.hospes.dbhelper.builder.models.DataType;
import ua.hospes.dbhelper.builder.models.DbColumn;

/**
 * @author Andrew Khloponin
 */
@SuppressWarnings("unchecked")
public interface Drivers {
    String name = "Drivers";
    Column<Integer> ID = DbColumn.ID();
    Column<String> NAME = DbColumn.newInstance("name", DataType.TEXT);
    Column<Integer> TEAM_ID = DbColumn.newInstance("team_id", DataType.INTEGER);


    static List<Column> columns() {
        return Arrays.asList(ID, NAME, TEAM_ID);
    }

    static CreateQuery create() {
        return new CreateQuery().tableName(name).columns(columns()).ifNotExists(true);
    }

    static DropQuery drop() {
        return new DropQuery().tableName(name).ifExists(true);
    }
}