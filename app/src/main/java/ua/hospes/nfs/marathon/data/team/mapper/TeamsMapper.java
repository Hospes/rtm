package ua.hospes.nfs.marathon.data.team.mapper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ua.hospes.nfs.marathon.core.db.tables.Teams;
import ua.hospes.nfs.marathon.data.team.models.TeamDb;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class TeamsMapper {
    public static TeamDb map(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Teams._ID));
        String name = cursor.getString(cursor.getColumnIndex(Teams.NAME));
        return new TeamDb(id, name);
    }

    public static Team map(TeamDb db) {
        return new Team(db.getId(), db.getName());
    }

    public static List<Team> map(List<TeamDb> dbs) {
        List<Team> result = new ArrayList<>();
        for (TeamDb db : dbs) result.add(TeamsMapper.map(db));
        return result;
    }


    public static TeamDb map(Team driver) {
        return new TeamDb(driver.getId(), driver.getName());
    }
}