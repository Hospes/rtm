package ua.hospes.nfs.marathon.data.race.mapper;

import android.database.Cursor;

import hugo.weaving.DebugLog;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.data.race.models.RaceItemDb;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class RaceMapper {
    public static RaceItemDb map(Cursor cursor) {
        int teamId = cursor.getInt(cursor.getColumnIndex(Race.TEAM_ID));
        RaceItemDb result = new RaceItemDb(teamId);
        result.setId(cursor.getInt(cursor.getColumnIndex(Race._ID)));
        result.setOrder(cursor.getInt(cursor.getColumnIndex(Race.ORDER)));
        return result;
    }


    public static RaceItem map(RaceItemDb db, Team team) {
        return new RaceItem(db.getId(), team);
    }


    @DebugLog
    public static RaceItemDb map(RaceItem item) {
        return new RaceItemDb(item.getTeam().getId());
    }
}