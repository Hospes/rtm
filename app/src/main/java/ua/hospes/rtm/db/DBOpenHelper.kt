package ua.hospes.rtm.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import ua.hospes.dbhelper.builder.CreateQuery
import ua.hospes.dbhelper.builder.DropQuery
import ua.hospes.rtm.db.tables.Race
import ua.hospes.rtm.db.tables.Sessions

private const val DB_NAME = "hospes.nfs.marathon"
private const val DB_VERSION = 2

internal class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createTables(db, arrayOf(Sessions.create(), Race.create()))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTables(db, arrayOf(Sessions.drop(), Race.drop()))
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTables(db, arrayOf(Sessions.drop(), Race.drop()))
        onCreate(db)
    }


    private fun dropTables(db: SQLiteDatabase, queries: Array<DropQuery>) =
            queries.forEach { db.execSQL(it.toString()) }

    private fun createTables(db: SQLiteDatabase, queries: Array<CreateQuery>) =
            queries.forEach { db.execSQL(it.toString()) }
}