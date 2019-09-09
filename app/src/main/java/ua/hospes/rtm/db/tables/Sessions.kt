package ua.hospes.rtm.db.tables

import java.util.Arrays

import ua.hospes.dbhelper.builder.CreateQuery
import ua.hospes.dbhelper.builder.DropQuery
import ua.hospes.dbhelper.builder.models.Column
import ua.hospes.dbhelper.builder.models.Constraint
import ua.hospes.dbhelper.builder.models.DataType
import ua.hospes.dbhelper.builder.models.DbColumn

/**
 * @author Andrew Khloponin
 */
interface Sessions {
    companion object {
        val name = "Sessions"
        val ID = DbColumn.ID()
        val TEAM_ID = DbColumn.newInstance("team_id", DataType.INTEGER, Constraint.NOT_NULL())
        val DRIVER_ID = DbColumn.newInstance("driver_id", DataType.INTEGER)
        val CAR_ID = DbColumn.newInstance("car_id", DataType.INTEGER)
        val RACE_START_TIME = DbColumn.newInstance("race_start_time", DataType.INTEGER, Constraint.DEFAULT(-1))
        val START_DURATION_TIME = DbColumn.newInstance("start_duration_time", DataType.INTEGER, Constraint.DEFAULT(-1))
        val END_DURATION_TIME = DbColumn.newInstance("end_duration_time", DataType.INTEGER, Constraint.DEFAULT(-1))
        val TYPE = DbColumn.newInstance("type", DataType.TEXT, Constraint.NOT_NULL())


        fun columns(): List<Column<*>> {
            return Arrays.asList(ID, TEAM_ID, DRIVER_ID, CAR_ID, RACE_START_TIME, START_DURATION_TIME, END_DURATION_TIME, TYPE)
        }

        fun create(): CreateQuery {
            return CreateQuery().tableName(name).columns(columns()).ifNotExists(true)
        }

        fun drop(): DropQuery {
            return DropQuery().tableName(name).ifExists(true)
        }
    }
}