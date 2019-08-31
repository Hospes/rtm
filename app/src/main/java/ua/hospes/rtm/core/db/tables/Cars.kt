package ua.hospes.rtm.core.db.tables

import java.util.Arrays

import ua.hospes.dbhelper.builder.CreateQuery
import ua.hospes.dbhelper.builder.DropQuery
import ua.hospes.dbhelper.builder.models.Column
import ua.hospes.dbhelper.builder.models.Constraint
import ua.hospes.dbhelper.builder.models.DataType
import ua.hospes.dbhelper.builder.models.DbColumn
import ua.hospes.rtm.domain.cars.Car

/**
 * @author Andrew Khloponin
 */
interface Cars {
    companion object {
        val name = "Cars"
        val ID = DbColumn.ID()
        val NUMBER = DbColumn.newInstance("numb", DataType.INTEGER, Constraint.NOT_NULL())
        val QUALITY = DbColumn.newInstance("quality", DataType.TEXT, Constraint.NOT_NULL(), Constraint.DEFAULT(Car.Quality.NORMAL.toString()))
        val BROKEN = DbColumn.newInstance("broken", DataType.INTEGER, Constraint.NOT_NULL(), Constraint.DEFAULT(0))


        fun columns(): List<Column<*>> {
            return Arrays.asList(ID, NUMBER, QUALITY, BROKEN)
        }

        fun create(): CreateQuery {
            return CreateQuery().tableName(name).columns(columns()).ifNotExists(true)
        }

        fun drop(): DropQuery {
            return DropQuery().tableName(name).ifExists(true)
        }
    }
}