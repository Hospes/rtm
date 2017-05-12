package ua.hospes.rtm.data.team.mapper;

import android.database.Cursor;

import java.util.List;

import ua.hospes.rtm.core.db.tables.Teams;
import ua.hospes.rtm.data.team.models.TeamDb;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class TeamsMapper {
    public static TeamDb map(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Teams._ID));
        String name = cursor.getString(cursor.getColumnIndex(Teams.NAME));
        return new TeamDb(id, name);
    }

    public static Team map(TeamDb db, List<Driver> drivers) {
        Team team = new Team(db.getId());
        team.setName(db.getName());
        team.setDrivers(drivers);
        return team;
    }

    public static TeamDb map(Team team) {
        return new TeamDb(team.getId(), team.getName());
    }
}