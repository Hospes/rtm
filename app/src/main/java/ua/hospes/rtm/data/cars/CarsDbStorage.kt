package ua.hospes.rtm.data.cars

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.google.common.primitives.Ints
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import ua.hospes.dbhelper.InsertResult
import ua.hospes.dbhelper.UpdateResult
import ua.hospes.dbhelper.builder.DeleteQuery
import ua.hospes.dbhelper.builder.SelectQuery
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.DbHelper
import ua.hospes.rtm.core.db.tables.Cars
import ua.hospes.rtm.core.db.tables.Sessions
import ua.hospes.rtm.data.cars.CarDb
import ua.hospes.rtm.data.cars.CarsMapper
import javax.inject.Inject

class CarsDbStorage @Inject constructor(private val dbHelper: DbHelper) {
    fun get(): Observable<CarDb> = dbHelper.querySingle({ CarsMapper.map(it) }, SelectQuery(Cars.name))

    fun get(vararg ids: Int): Observable<CarDb> =
            dbHelper.querySingle({ CarsMapper.map(it) }, SelectQuery(Cars.name).where(Condition.`in`(Cars.ID, Ints.asList(*ids))))


    fun getNotInRace(): Observable<CarDb> =
            //SELECT c.* FROM Cars AS c WHERE c._id NOT IN ( SELECT s.car_id FROM Sessions AS s WHERE s.end_time = -1 )
            dbHelper.querySingle(Function<Cursor, CarDb> { CarsMapper.map(it) },
                    "SELECT c.rowid, c.* FROM " + Cars.name + " c" +
                            " WHERE c." + Cars.ID.name() + " NOT IN " +
                            "(" +
                            " SELECT s." + Sessions.CAR_ID.name() + " FROM " + Sessions.name + " AS s" +
                            " WHERE s." + Sessions.END_DURATION_TIME.name() + " = ?" +
                            " )",
                    (-1).toString()
            )


    fun listen(): Observable<List<CarDb>> = dbHelper.query({ CarsMapper.map(it) }, SelectQuery(Cars.name))


    fun add(car: CarDb): Observable<InsertResult<CarDb>> = dbHelper.insert(Cars.name, SQLiteDatabase.CONFLICT_ABORT, car)

    fun update(car: CarDb): Observable<UpdateResult<CarDb>> = dbHelper.update(UpdateQuery(Cars.name).where(Condition.eq(Cars.ID, car.id)), car)


    fun remove(id: Int?): Single<Int> = dbHelper.delete(DeleteQuery(Cars.name).where(Condition.eq(Cars.ID, id)))

    fun remove(carDb: CarDb): Single<Int> = dbHelper.delete(DeleteQuery(Cars.name).where(Condition.eq(Cars.ID, carDb.id)))

    fun removeAll(): Single<Int> = dbHelper.delete(DeleteQuery(Cars.name))
}