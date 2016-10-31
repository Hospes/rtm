package ua.hospes.nfs.marathon.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hugo.weaving.DebugLog;
import ua.hospes.nfs.marathon.core.db.tables.Cars;
import ua.hospes.nfs.marathon.core.db.tables.Drivers;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.core.db.tables.Teams;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "hospes.nfs.marathon";
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


    private static final String CREATE_CARS_TABLE_SQL = CREATE_TABLE + Cars.name + " (" +
            Cars._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            Cars.NUMBER + INTEGER_TYPE + DEFAULT + " 0 " + COMMA_SEP +
            Cars.RATING + INTEGER_TYPE + DEFAULT + " 0 " +
            " );";

    private static final String CREATE_DRIVERS_TABLE_SQL = CREATE_TABLE + Drivers.name + " (" +
            Drivers._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            Drivers.NAME + TEXT_TYPE + COMMA_SEP +
            Drivers.TEAM_ID + INTEGER_TYPE +
            " );";

    private static final String CREATE_TEAMS_TABLE_SQL = CREATE_TABLE + Teams.name + " (" +
            Teams._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            Teams.NAME + TEXT_TYPE +
            " );";

    private static final String CREATE_SESSIONS_TABLE_SQL = CREATE_TABLE + Sessions.name + " (" +
            Sessions._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            Sessions.TEAM_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
            Sessions.DRIVER_ID + INTEGER_TYPE + COMMA_SEP +
            Sessions.CAR_ID + INTEGER_TYPE + COMMA_SEP +
            Sessions.START_DURATION_TIME + INTEGER_TYPE + DEFAULT + " -1 " + COMMA_SEP +
            Sessions.END_DURATION_TIME + INTEGER_TYPE + DEFAULT + " -1 " + COMMA_SEP +
            Sessions.TYPE + TEXT_TYPE + NOT_NULL +
            " );";

    private static final String CREATE_RACE_TABLE_SQL = CREATE_TABLE + Race.name + " (" +
            Race._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
            Race.TEAM_ID + INTEGER_TYPE + UNIQUE + NOT_NULL + COMMA_SEP +
            Race.TEAM_NUMBER + INTEGER_TYPE + DEFAULT + " -1 " + COMMA_SEP +
            Race.SESSION_ID + INTEGER_TYPE + COMMA_SEP +
            Race.ORDER + INTEGER_TYPE +
            " );";


    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @DebugLog
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CARS_TABLE_SQL);
        db.execSQL(CREATE_DRIVERS_TABLE_SQL);
        db.execSQL(CREATE_TEAMS_TABLE_SQL);
        db.execSQL(CREATE_SESSIONS_TABLE_SQL);
        db.execSQL(CREATE_RACE_TABLE_SQL);
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
        dropTables(db, Cars.name, Drivers.name, Teams.name, Sessions.name, Race.name);
    }

    @DebugLog
    private void dropTables(SQLiteDatabase db, String... tables) {
        for (String table : tables) db.execSQL(DROP_TABLE_IF_EXISTS + table);
    }
}