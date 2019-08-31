package ua.hospes.rtm.core.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import ua.hospes.dbhelper.builder.CreateQuery
import ua.hospes.dbhelper.builder.DropQuery
import ua.hospes.rtm.core.db.tables.Cars
import ua.hospes.rtm.core.db.tables.Drivers
import ua.hospes.rtm.core.db.tables.Race
import ua.hospes.rtm.core.db.tables.Sessions
import ua.hospes.rtm.core.db.tables.Teams

internal class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        createTables(db, arrayOf(Cars.create(), Drivers.create(), Teams.create(), Sessions.create(), Race.create()))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTables(db, arrayOf(Cars.drop(), Drivers.drop(), Teams.drop(), Sessions.drop(), Race.drop()))
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTables(db, arrayOf(Cars.drop(), Drivers.drop(), Teams.drop(), Sessions.drop(), Race.drop()))
        onCreate(db)
    }


    private fun dropTables(db: SQLiteDatabase, queries: Array<DropQuery>) {
        for (query in queries) db.execSQL(query.toString())
    }

    private fun createTables(db: SQLiteDatabase, queries: Array<CreateQuery>) {
        for (query in queries) db.execSQL(query.toString())
    }

    companion object {
        private val DB_NAME = "hospes.nfs.marathon"
        private val DB_VERSION = 2
    }
}