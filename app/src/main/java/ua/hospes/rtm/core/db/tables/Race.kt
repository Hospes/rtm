package ua.hospes.rtm.core.db.tables

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
interface Race {
    companion object {
        val name = "Race"
        val ID = DbColumn.ID()
        val TEAM_ID = DbColumn.newInstance("team_id", DataType.INTEGER, Constraint.UNIQUE(), Constraint.NOT_NULL())
        val TEAM_NUMBER = DbColumn.newInstance("team_number", DataType.INTEGER, Constraint.DEFAULT(-1))
        val SESSION_ID = DbColumn.newInstance("session_id", DataType.INTEGER)
        val ORDER = DbColumn.newInstance("order_val", DataType.INTEGER)


        fun columns(): List<Column<*>> {
            return Arrays.asList<Column<*>>(ID, TEAM_ID, TEAM_NUMBER, SESSION_ID, ORDER)
        }

        fun create(): CreateQuery {
            return CreateQuery().tableName(name).columns(columns()).ifNotExists(true)
        }

        fun drop(): DropQuery {
            return DropQuery().tableName(name).ifExists(true)
        }
    }
}