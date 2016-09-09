package ua.hospes.nfs.marathon.data.sessions.mapper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.data.sessions.models.SessionDb;
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
        result.setType(cursor.getInt(cursor.getColumnIndex(Sessions.TYPE)));
        return result;
    }


    public static Session map(SessionDb db) {
        return new Session(0, null);
    }

    public static List<Session> map(List<SessionDb> dbs) {
        List<Session> result = new ArrayList<>();
        for (SessionDb db : dbs) result.add(SessionsMapper.map(db));
        return result;
    }


    public static SessionDb map(Session session) {
        return new SessionDb(session.getTeam().getId());
    }
}