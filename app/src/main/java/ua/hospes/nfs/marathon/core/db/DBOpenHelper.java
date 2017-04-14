package ua.hospes.nfs.marathon.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.hospes.dbhelper.AbsDbTable;
import ua.hospes.nfs.marathon.core.db.tables.Cars;
import ua.hospes.nfs.marathon.core.db.tables.Drivers;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.core.db.tables.Teams;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "DBOpenHelper";
    private static String DB_NAME = "hospes.nfs.marathon";
    private static int DB_VERSION = 2;


    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(new Cars().create());
        db.execSQL(new Drivers().create());
        db.execSQL(new Teams().create());
        db.execSQL(new Sessions().create());
        db.execSQL(new Race().create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cleanupDatabase(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cleanupDatabase(db);
        onCreate(db);
    }


    private void cleanupDatabase(SQLiteDatabase db) {
        dropTables(db, new AbsDbTable[]{new Cars(), new Drivers(), new Teams(), new Sessions(), new Race()});
    }

    private void dropTables(SQLiteDatabase db, AbsDbTable[] tables) {
        for (AbsDbTable table : tables) db.execSQL(table.drop());
    }
}