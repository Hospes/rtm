package ua.hospes.nfs.marathon.core.db;

import android.content.Context;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.schedulers.Schedulers;
import ua.hospes.dbhelper.AbsDbHelper;

@Singleton
public class DbHelper extends AbsDbHelper {
    private static final String LOG_TAG = "DbHelper";
    private final BriteDatabase db;

    @Inject
    public DbHelper(Context context) {
        SqlBrite sqlBrite = new SqlBrite.Builder().logger(message -> Log.d(LOG_TAG, message)).build();

        db = sqlBrite.wrapDatabaseHelper(new DBOpenHelper(context), Schedulers.io());
    }


    public BriteDatabase getDb() {
        return db;
    }
}