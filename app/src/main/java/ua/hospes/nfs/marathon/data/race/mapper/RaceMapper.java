package ua.hospes.nfs.marathon.data.race.mapper;

import android.database.Cursor;

import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.data.race.models.RaceItemDb;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class RaceMapper {
    public static RaceItemDb map(Cursor cursor) {
        int teamId = cursor.getInt(cursor.getColumnIndex(Race.TEAM_ID));
        RaceItemDb result = new RaceItemDb(teamId);
        result.setId(cursor.getInt(cursor.getColumnIndex(Race._ID)));
        result.setTeamNumber(cursor.getInt(cursor.getColumnIndex(Race.TEAM_NUMBER)));
        result.setSessionId(cursor.getInt(cursor.getColumnIndex(Race.SESSION_ID)));
        result.setOrder(cursor.getInt(cursor.getColumnIndex(Race.ORDER)));
        return result;
    }

    public static RaceItem map(RaceItemDb db, Team team, Session session) {
        RaceItem result = new RaceItem(db.getId(), team);
        result.setTeamNumber(db.getTeamNumber());
        result.setSession(session);
        return result;
    }

    public static RaceItemDb map(RaceItem item) {
        RaceItemDb db = new RaceItemDb(item.getTeam().getId());

        db.setId(item.getId());
        db.setTeamNumber(item.getTeamNumber());
        if (item.getSession() != null)
            db.setSessionId(item.getSession().getId());

        return db;
    }
}