package ua.hospes.nfs.marathon.data.sessions.mapper;

import android.database.Cursor;

import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.data.sessions.models.SessionDb;
import ua.hospes.nfs.marathon.domain.cars.models.Car;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
public class SessionsMapper {
    public static SessionDb map(Cursor cursor) {
        int teamId = cursor.getInt(cursor.getColumnIndex(Sessions.TEAM_ID));
        SessionDb result = new SessionDb(teamId);
        result.setId(cursor.getInt(cursor.getColumnIndex(Sessions._ID)));
        result.setDriverId(cursor.getInt(cursor.getColumnIndex(Sessions.DRIVER_ID)));
        result.setCarId(cursor.getInt(cursor.getColumnIndex(Sessions.CAR_ID)));
        result.setStartDurationTime(cursor.getLong(cursor.getColumnIndex(Sessions.START_DURATION_TIME)));
        result.setEndDurationTime(cursor.getLong(cursor.getColumnIndex(Sessions.END_DURATION_TIME)));
        result.setType(cursor.getString(cursor.getColumnIndex(Sessions.TYPE)));
        return result;
    }

    public static Session map(SessionDb db, Driver driver, Car car) {
        Session session = new Session(db.getId(), db.getTeamId());
        session.setDriver(driver);
        session.setCar(car);
        session.setStartDurationTime(db.getStartDurationTime());
        session.setEndDurationTime(db.getEndDurationTime());
        session.setType(db.getType());
        return session;
    }

    public static SessionDb map(Session session) {
        SessionDb item = new SessionDb(session.getTeamId());
        if (session.getDriver() != null)
            item.setDriverId(session.getDriver().getId());
        if (session.getCar() != null)
            item.setCarId(session.getCar().getId());
        item.setStartDurationTime(session.getStartDurationTime());
        item.setEndDurationTime(session.getEndDurationTime());
        item.setType(session.getType().name());
        return item;
    }
}