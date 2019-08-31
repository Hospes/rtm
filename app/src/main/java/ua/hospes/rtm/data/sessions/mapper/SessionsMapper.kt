package ua.hospes.rtm.data.sessions.mapper

import android.database.Cursor

import ua.hospes.rtm.core.db.tables.Sessions
import ua.hospes.rtm.data.sessions.models.SessionDb
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.Optional

/**
 * @author Andrew Khloponin
 */
object SessionsMapper {
    fun map(cursor: Cursor): SessionDb {
        val teamId = cursor.getInt(cursor.getColumnIndex(Sessions.TEAM_ID.name()))
        val result = SessionDb(teamId)
        result.id = cursor.getInt(cursor.getColumnIndex(Sessions.ID.name()))
        result.driverId = cursor.getInt(cursor.getColumnIndex(Sessions.DRIVER_ID.name()))
        result.carId = cursor.getInt(cursor.getColumnIndex(Sessions.CAR_ID.name()))
        result.raceStartTime = cursor.getLong(cursor.getColumnIndex(Sessions.RACE_START_TIME.name()))
        result.startDurationTime = cursor.getLong(cursor.getColumnIndex(Sessions.START_DURATION_TIME.name()))
        result.endDurationTime = cursor.getLong(cursor.getColumnIndex(Sessions.END_DURATION_TIME.name()))
        result.type = cursor.getString(cursor.getColumnIndex(Sessions.TYPE.name()))
        return result
    }

    fun map(db: SessionDb, driver: Optional<Driver>, car: Optional<Car>): Session {
        val session = Session(db.id, db.teamId)
        session.driver = driver.get()
        session.car = car.get()
        session.raceStartTime = db.raceStartTime
        session.startDurationTime = db.startDurationTime
        session.endDurationTime = db.endDurationTime
        session.setType(db.type)
        return session
    }

    fun map(session: Session): SessionDb {
        val item = SessionDb(session.teamId)
        if (session.driver != null)
            item.driverId = session.driver!!.id
        if (session.car != null)
            item.carId = session.car!!.id
        item.raceStartTime = session.raceStartTime
        item.startDurationTime = session.startDurationTime
        item.endDurationTime = session.endDurationTime
        item.type = session.type.name
        return item
    }
}