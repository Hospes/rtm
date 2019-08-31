package ua.hospes.rtm.core.db.tables

import java.util.Arrays

import ua.hospes.dbhelper.builder.CreateQuery
import ua.hospes.dbhelper.builder.DropQuery
import ua.hospes.dbhelper.builder.models.Column
import ua.hospes.dbhelper.builder.models.DataType
import ua.hospes.dbhelper.builder.models.DbColumn

/**
 * @author Andrew Khloponin
 */
interface Drivers {
    companion object {
        val name = "Drivers"
        val ID = DbColumn.ID()
        val NAME = DbColumn.newInstance("name", DataType.TEXT)
        val TEAM_ID = DbColumn.newInstance("team_id", DataType.INTEGER)


        fun columns(): List<Column<*>> {
            return Arrays.asList(ID, NAME, TEAM_ID)
        }

        fun create(): CreateQuery {
            return CreateQuery().tableName(name).columns(columns()).ifNotExists(true)
        }

        fun drop(): DropQuery {
            return DropQuery().tableName(name).ifExists(true)
        }
    }
}