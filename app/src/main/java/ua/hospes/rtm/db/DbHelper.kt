package ua.hospes.rtm.db

import android.content.ContentValues
import android.content.Context
import android.util.Log

import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite

import java.util.Arrays

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ua.hospes.dbhelper.AbsDbHelper
import ua.hospes.dbhelper.UpdateResult
import ua.hospes.dbhelper.builder.UpdateQuery

@Singleton
class DbHelper @Inject
constructor(context: Context) : AbsDbHelper() {
    private val db: BriteDatabase

    init {
        val sqlBrite = SqlBrite.Builder().logger { message -> Log.d(LOG_TAG, message) }.build()

        db = sqlBrite.wrapDatabaseHelper(DBOpenHelper(context), Schedulers.io())
    }


    public override fun getDb(): BriteDatabase {
        return db
    }


    fun updateCV(query: UpdateQuery, vararg cvs: ContentValues): Observable<UpdateResult<ContentValues>> {
        return updateCV(query, Arrays.asList(*cvs))
    }

    fun updateCV(query: UpdateQuery?, cvs: Iterable<ContentValues>?): Observable<UpdateResult<ContentValues>> {
        return if (query != null && cvs != null)
            Observable.create { emitter ->
                val transaction = this@DbHelper.getDb().newTransaction()
                try {
                    for (cv in cvs) {
                        val updateResult = this@DbHelper.getDb().update(query.table, cv, query.whereClause, *query.whereArgs).toLong()
                        emitter.onNext(UpdateResult(updateResult, cv))
                    }
                    transaction.markSuccessful()
                } finally {
                    transaction.end()
                    emitter.onComplete()
                }
            }
        else
            Observable.empty()
    }

    companion object {
        private val LOG_TAG = "DbHelper"
    }
}