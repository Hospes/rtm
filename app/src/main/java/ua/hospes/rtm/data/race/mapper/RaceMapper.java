package ua.hospes.rtm.data.race.mapper;

import android.database.Cursor;

import ua.hospes.rtm.core.db.tables.Race;
import ua.hospes.rtm.data.race.models.RaceItemDb;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.Session;
import ua.hospes.rtm.domain.team.Team;
import ua.hospes.rtm.utils.Optional;

public class RaceMapper {
    public static RaceItemDb map(Cursor cursor) {
        int teamId = cursor.getInt(cursor.getColumnIndex(Race.TEAM_ID.name()));
        RaceItemDb result = new RaceItemDb(teamId);
        result.setId(cursor.getInt(cursor.getColumnIndex(Race.ID.name())));
        result.setTeamNumber(cursor.getInt(cursor.getColumnIndex(Race.TEAM_NUMBER.name())));
        result.setSessionId(cursor.getInt(cursor.getColumnIndex(Race.SESSION_ID.name())));
        result.setOrder(cursor.getInt(cursor.getColumnIndex(Race.ORDER.name())));
        return result;
    }

    public static RaceItem map(RaceItemDb db, Optional<Team> team, Optional<Session> session) {
        RaceItem result = new RaceItem(db.getId(), team.get());
        result.setTeamNumber(db.getTeamNumber());
        result.setSession(session.get());
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