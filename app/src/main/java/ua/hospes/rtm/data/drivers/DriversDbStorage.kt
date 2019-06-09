package ua.hospes.rtm.data.drivers

import android.content.ContentValues
import com.google.common.primitives.Ints
import io.reactivex.Completable
import io.reactivex.Observable
import ua.hospes.dbhelper.InsertResult
import ua.hospes.dbhelper.UpdateResult
import ua.hospes.dbhelper.builder.DeleteQuery
import ua.hospes.dbhelper.builder.SelectQuery
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.DbHelper
import ua.hospes.rtm.core.db.tables.Drivers
import javax.inject.Inject

class DriversDbStorage @Inject constructor(private val dbHelper: DbHelper) {

    fun listen(): Observable<List<DriverDb>> = dbHelper.query({ DriversMapper.map(it) }, SelectQuery(Drivers.name))

    fun get(): Observable<DriverDb> = dbHelper.querySingle({ DriversMapper.map(it) }, SelectQuery(Drivers.name))

    fun get(vararg ids: Int): Observable<DriverDb> =
            dbHelper.querySingle({ DriversMapper.map(it) }, SelectQuery(Drivers.name).where(Condition.`in`(Drivers.ID, Ints.asList(*ids))))

    fun getTeamById(teamId: Int): Observable<DriverDb> =
            dbHelper.querySingle({ DriversMapper.map(it) }, SelectQuery(Drivers.name).where(Condition.eq(Drivers.TEAM_ID, teamId)))


    fun add(driver: DriverDb): Observable<InsertResult<DriverDb>> = dbHelper.insert(Drivers.name, 0, driver)

    fun update(driver: DriverDb): Observable<UpdateResult<DriverDb>> =
            dbHelper.update(UpdateQuery(Drivers.name).where(Condition.eq(Drivers.ID, driver.id)), driver)


    fun removeDriversFromTeam(teamId: Int): Completable {
        val cv = ContentValues()
        cv.put(Drivers.TEAM_ID.name(), -1)

        return dbHelper.updateCV(UpdateQuery(Drivers.name).where(Condition.eq(Drivers.TEAM_ID, teamId)), cv)
                .map { tUpdateResult -> tUpdateResult.result > 0 }.toList().ignoreElement()
    }

    fun addDriversToTeam(teamId: Int, vararg driverIds: Int): Completable {
        val cv = ContentValues()
        cv.put(Drivers.TEAM_ID.name(), teamId)

        return dbHelper.updateCV(UpdateQuery(Drivers.name).where(Condition.`in`(Drivers.ID, Ints.asList(*driverIds))), cv)
                .map { tUpdateResult -> tUpdateResult.result > 0 }.toList().ignoreElement()
    }


    fun remove(id: Int): Completable = dbHelper.delete(DeleteQuery(Drivers.name).where(Condition.eq(Drivers.ID, id))).ignoreElement()

    fun removeAll(): Completable = dbHelper.delete(DeleteQuery(Drivers.name)).ignoreElement()
}