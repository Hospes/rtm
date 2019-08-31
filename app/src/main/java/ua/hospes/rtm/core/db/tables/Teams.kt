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
interface Teams {
    companion object {
        val name = "Teams"
        val ID = DbColumn.ID()
        val NAME = DbColumn.newInstance("name", DataType.TEXT)


        fun columns(): List<Column<*>> {
            return Arrays.asList(ID, NAME)
        }

        fun create(): CreateQuery {
            return CreateQuery().tableName(name).columns(columns()).ifNotExists(true)
        }

        fun drop(): DropQuery {
            return DropQuery().tableName(name).ifExists(true)
        }
    }
}