package ua.hospes.nfs.marathon.core.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ua.hospes.nfs.marathon.core.db.models.InsertResult;
import ua.hospes.nfs.marathon.core.db.models.UpdateResult;

@Singleton
public class DbHelper {
    private static final String LOG_TAG = "DbHelper";
    private final BriteDatabase db;

    @Inject
    public DbHelper(Context context) {
        SqlBrite sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Log.d(LOG_TAG, message);
            }
        });

        db = sqlBrite.wrapDatabaseHelper(new DBOpenHelper(context), Schedulers.io());
    }


    public BriteDatabase getDb() {
        return db;
    }


    //region Query
    public final Observable<SqlBrite.Query> query(final String table, String sql, final String... sqlArgs) {
        return db.createQuery(table, sql, sqlArgs);
    }

    public final Observable<SqlBrite.Query> query(final QueryBuilder builder) {
        return db.createQuery(builder.getTable(), builder.getFullSelection(), builder.getSelectionArgs());
    }

    public final <T> Observable<List<T>> query(final Func1<Cursor, T> mapper, final QueryBuilder builder) {
        return db.createQuery(builder.getTable(), builder.getFullSelection(), builder.getSelectionArgs())
                .mapToList(mapper);
    }

    public final <T> Observable<T> singleQuery(final Func1<Cursor, T> mapper, final String sql, final String... sqlArgs) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Cursor cursor = db.query(sql, sqlArgs);
                if (cursor != null) {
                    try {
                        while (cursor.moveToNext() && !subscriber.isUnsubscribed()) {
                            subscriber.onNext(mapper.call(cursor));
                        }
                    } finally {
                        cursor.close();
                    }
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public final <T> Observable<T> singleQuery(final Func1<Cursor, T> mapper, final QueryBuilder builder) {
        if (mapper == null || builder == null) return Observable.empty();
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Cursor cursor = db.query(builder.getFullSelection(), builder.getSelectionArgs());
                if (cursor != null) {
                    try {
                        while (cursor.moveToNext() && !subscriber.isUnsubscribed()) {
                            subscriber.onNext(mapper.call(cursor));
                        }
                    } finally {
                        cursor.close();
                    }
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }
    //endregion


    //region Insert
    public final <T extends ModelBaseInterface> Observable<InsertResult<T>> insert(final String table, final T cv) {
        return insert(table, Collections.singletonList(cv));
    }

    @SafeVarargs
    public final <T extends ModelBaseInterface> Observable<InsertResult<T>> insert(final String table, final T... cvs) {
        return insert(table, Arrays.asList(cvs));
    }

    public final <T extends ModelBaseInterface> Observable<InsertResult<T>> insert(final String table, final List<T> cvs) {
        return insert(table, cvs, SQLiteDatabase.CONFLICT_NONE);
    }

    public final <T extends ModelBaseInterface> Observable<InsertResult<T>> insertOrReplace(final String table, final T cv) {
        return insertOrReplace(table, Collections.singletonList(cv));
    }

    @SafeVarargs
    public final <T extends ModelBaseInterface> Observable<InsertResult<T>> insertOrReplace(final String table, final T... cvs) {
        return insertOrReplace(table, Arrays.asList(cvs));
    }

    public final <T extends ModelBaseInterface> Observable<InsertResult<T>> insertOrReplace(final String table, final Iterable<T> cvs) {
        return insert(table, cvs, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public final <T extends ModelBaseInterface> Observable<InsertResult<T>> clearAndInsert(final String table, final Iterable<T> cvs) {
        return Observable.create(new Observable.OnSubscribe<InsertResult<T>>() {
            @Override
            public void call(Subscriber<? super InsertResult<T>> subscriber) {
                BriteDatabase.Transaction transaction = db.newTransaction();
                try {
                    db.delete(table, null);

                    for (T cv : cvs) {
                        long insertResult = db.insert(table, cv.toContentValues(), SQLiteDatabase.CONFLICT_NONE);
                        subscriber.onNext(new InsertResult<>(insertResult, cv));
                    }
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    subscriber.onCompleted();
                }
            }
        });
    }


    private <T extends ModelBaseInterface> Observable<InsertResult<T>> insert(final String table, final Iterable<T> cvs, @BriteDatabase.ConflictAlgorithm int conflictAlgorithm) {
        return Observable.create(new Observable.OnSubscribe<InsertResult<T>>() {
            @Override
            public void call(Subscriber<? super InsertResult<T>> subscriber) {
                BriteDatabase.Transaction transaction = db.newTransaction();
                try {
                    for (T cv : cvs) {
                        long insertResult = db.insert(table, cv.toContentValues(), conflictAlgorithm);
                        subscriber.onNext(new InsertResult<>(insertResult, cv));
                    }
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    subscriber.onCompleted();
                }
            }
        });
    }
    //endregion


    //region Update
    @RxLogObservable
    public final <T extends ModelBaseInterface> Observable<UpdateResult<T>> update(final QueryBuilder builder, final T cv) {
        if (builder == null || cv == null) return Observable.empty();
        return Observable.create(new Observable.OnSubscribe<UpdateResult<T>>() {
            @Override
            public void call(Subscriber<? super UpdateResult<T>> subscriber) {
                long updateResult = db.update(builder.getTable(), cv.toContentValues(), builder.getSelection(), builder.getSelectionArgs());
                subscriber.onNext(new UpdateResult<>(updateResult, cv));
                subscriber.onCompleted();
            }
        });
    }

    @SafeVarargs
    public final <T extends ModelBaseInterface> Observable<UpdateResult<T>> update(final QueryBuilder builder, final T... cvs) {
        return update(builder, Arrays.asList(cvs));
    }

    public final <T extends ModelBaseInterface> Observable<UpdateResult<T>> update(final QueryBuilder builder, final List<T> cvs) {
        if (builder == null || cvs == null) return Observable.empty();
        return Observable.create(new Observable.OnSubscribe<UpdateResult<T>>() {
            @Override
            public void call(Subscriber<? super UpdateResult<T>> subscriber) {
                BriteDatabase.Transaction transaction = db.newTransaction();
                try {
                    for (T cv : cvs) {
                        long updateResult = db.update(builder.getTable(), cv.toContentValues(), builder.getSelection(), builder.getSelectionArgs());
                        subscriber.onNext(new UpdateResult<>(updateResult, cv));
                    }
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    subscriber.onCompleted();
                }
            }
        });
    }
    //endregion


    //region Delete
    public final Observable<Integer> delete(@NonNull final QueryBuilder builder) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(db.delete(builder.getTable(), builder.getSelection(), builder.getSelectionArgs()));
                subscriber.onCompleted();
            }
        });
    }
    //endregion


    public Observable<Long> multiOperationTransaction(final Iterable<Operation> operations) {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                BriteDatabase.Transaction transaction = db.newTransaction();
                try {
                    for (Operation operation : operations) {
                        subscriber.onNext(operation.doOperation(db));
                    }
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    subscriber.onCompleted();
                }
            }
        });
    }
}