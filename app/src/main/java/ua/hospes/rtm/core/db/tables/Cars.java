package ua.hospes.rtm.core.db.tables;

import java.util.Arrays;
import java.util.List;

import ua.hospes.dbhelper.builder.CreateQuery;
import ua.hospes.dbhelper.builder.DropQuery;
import ua.hospes.dbhelper.builder.models.Column;
import ua.hospes.dbhelper.builder.models.Constraint;
import ua.hospes.dbhelper.builder.models.DataType;
import ua.hospes.dbhelper.builder.models.DbColumn;
import ua.hospes.rtm.domain.cars.Car;

/**
 * @author Andrew Khloponin
 */
@SuppressWarnings("unchecked")
public interface Cars {
    String name = "Cars";
    Column<Integer> ID = DbColumn.ID();
    Column<Integer> NUMBER = DbColumn.newInstance("numb", DataType.INTEGER, Constraint.NOT_NULL());
    Column<String> QUALITY = DbColumn.newInstance("quality", DataType.TEXT, Constraint.NOT_NULL(), Constraint.DEFAULT(Car.Quality.NORMAL.toString()));
    Column<Integer> BROKEN = DbColumn.newInstance("broken", DataType.INTEGER, Constraint.NOT_NULL(), Constraint.DEFAULT(0));


    static List<Column> columns() {
        return Arrays.asList(ID, NUMBER, QUALITY, BROKEN);
    }

    static CreateQuery create() {
        return new CreateQuery().tableName(name).columns(columns()).ifNotExists(true);
    }

    static DropQuery drop() {
        return new DropQuery().tableName(name).ifExists(true);
    }
}