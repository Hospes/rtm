package ua.hospes.rtm.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.hospes.dbhelper.builder.CreateQuery;
import ua.hospes.dbhelper.builder.DropQuery;
import ua.hospes.rtm.core.db.tables.Cars;
import ua.hospes.rtm.core.db.tables.Drivers;
import ua.hospes.rtm.core.db.tables.Race;
import ua.hospes.rtm.core.db.tables.Sessions;
import ua.hospes.rtm.core.db.tables.Teams;

class DBOpenHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "hospes.nfs.marathon";
    private static int DB_VERSION = 2;


    DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db, new CreateQuery[]{Cars.create(), Drivers.create(), Teams.create(), Sessions.create(), Race.create()});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db, new DropQuery[]{Cars.drop(), Drivers.drop(), Teams.drop(), Sessions.drop(), Race.drop()});
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db, new DropQuery[]{Cars.drop(), Drivers.drop(), Teams.drop(), Sessions.drop(), Race.drop()});
        onCreate(db);
    }


    private void dropTables(SQLiteDatabase db, DropQuery[] queries) {
        for (DropQuery query : queries) db.execSQL(query.toString());
    }

    private void createTables(SQLiteDatabase db, CreateQuery[] queries) {
        for (CreateQuery query : queries) db.execSQL(query.toString());
    }
}