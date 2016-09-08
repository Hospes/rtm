package ua.hospes.nfs.marathon.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hugo.weaving.DebugLog;
import ua.hospes.nfs.marathon.core.db.tables.Drivers;
import ua.hospes.nfs.marathon.core.db.tables.Teams;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "DBOpenHelper";
    private static String DB_NAME = "bwww.lobby.db";
    private static int DB_VERSION = 1;


    private static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String REAL_TYPE = " REAL ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String UNIQUE = " UNIQUE ";
    private static final String NOT_NULL_UNIQUE = NOT_NULL + UNIQUE;
    private static final String TEXT_TYPE = " TEXT ";
    private static final String BLOB_TYPE = " BLOB ";
    private static final String REFERENCES = " REFERENCES ";
    private static final String ON_DELETE_CASCADE = " ON DELETE CASCADE ";
    private static final String COMMA_SEP = ", ";
    private static final String DEFAULT = " DEFAULT ";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";


    private static final String CREATE_DRIVERS_TABLE_SQL = CREATE_TABLE + Drivers.name + " (" +
            Drivers._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            Drivers.NAME + TEXT_TYPE + COMMA_SEP +
            Drivers.TEAM_ID + INTEGER_TYPE +
            " );";

    private static final String CREATE_TEAMS_TABLE_SQL = CREATE_TABLE + Teams.name + " (" +
            Teams._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            Teams.NAME + TEXT_TYPE +
            " );";


    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @DebugLog
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DRIVERS_TABLE_SQL);
        db.execSQL(CREATE_TEAMS_TABLE_SQL);
    }

    @DebugLog
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cleanupDatabase(db);
        onCreate(db);
    }

    @DebugLog
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cleanupDatabase(db);
        onCreate(db);
    }


    @DebugLog
    private void cleanupDatabase(SQLiteDatabase db) {
        dropTables(db, new String[]{Drivers.name, Teams.name});
    }

    @DebugLog
    private void dropTables(SQLiteDatabase db, String[] tables) {
        for (String table : tables) db.execSQL(DROP_TABLE_IF_EXISTS + table);
    }
}