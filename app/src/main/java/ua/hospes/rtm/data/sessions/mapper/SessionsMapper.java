package ua.hospes.rtm.data.sessions.mapper;

import android.database.Cursor;

import ua.hospes.rtm.core.db.tables.Sessions;
import ua.hospes.rtm.data.sessions.models.SessionDb;
import ua.hospes.rtm.domain.cars.Car;
import ua.hospes.rtm.domain.drivers.Driver;
import ua.hospes.rtm.domain.sessions.Session;
import ua.hospes.rtm.utils.Optional;

/**
 * @author Andrew Khloponin
 */
public class SessionsMapper {
    public static SessionDb map(Cursor cursor) {
        int teamId = cursor.getInt(cursor.getColumnIndex(Sessions.TEAM_ID.name()));
        SessionDb result = new SessionDb(teamId);
        result.setId(cursor.getInt(cursor.getColumnIndex(Sessions.ID.name())));
        result.setDriverId(cursor.getInt(cursor.getColumnIndex(Sessions.DRIVER_ID.name())));
        result.setCarId(cursor.getInt(cursor.getColumnIndex(Sessions.CAR_ID.name())));
        result.setRaceStartTime(cursor.getLong(cursor.getColumnIndex(Sessions.RACE_START_TIME.name())));
        result.setStartDurationTime(cursor.getLong(cursor.getColumnIndex(Sessions.START_DURATION_TIME.name())));
        result.setEndDurationTime(cursor.getLong(cursor.getColumnIndex(Sessions.END_DURATION_TIME.name())));
        result.setType(cursor.getString(cursor.getColumnIndex(Sessions.TYPE.name())));
        return result;
    }

    public static Session map(SessionDb db, Optional<Driver> driver, Optional<Car> car) {
        Session session = new Session(db.getId(), db.getTeamId());
        session.setDriver(driver.get());
        session.setCar(car.get());
        session.setRaceStartTime(db.getRaceStartTime());
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
        item.setRaceStartTime(session.getRaceStartTime());
        item.setStartDurationTime(session.getStartDurationTime());
        item.setEndDurationTime(session.getEndDurationTime());
        item.setType(session.getType().name());
        return item;
    }
}