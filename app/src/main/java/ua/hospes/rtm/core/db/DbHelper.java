package ua.hospes.rtm.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ua.hospes.dbhelper.AbsDbHelper;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.dbhelper.builder.UpdateQuery;

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


    public final Observable<UpdateResult<ContentValues>> updateCV(UpdateQuery query, ContentValues... cvs) {
        return updateCV(query, Arrays.asList(cvs));
    }

    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public final Observable<UpdateResult<ContentValues>> updateCV(UpdateQuery query, Iterable<ContentValues> cvs) {
        return query != null && cvs != null ? Observable.create(emitter -> {
            BriteDatabase.Transaction transaction = DbHelper.this.getDb().newTransaction();
            try {
                for (ContentValues cv : cvs) {
                    long updateResult = (long) DbHelper.this.getDb().update(query.getTable(), cv, query.getWhereClause(), query.getWhereArgs());
                    emitter.onNext(new UpdateResult(updateResult, cv));
                }
                transaction.markSuccessful();
            } finally {
                transaction.end();
                emitter.onComplete();
            }
        }) : Observable.empty();
    }
}