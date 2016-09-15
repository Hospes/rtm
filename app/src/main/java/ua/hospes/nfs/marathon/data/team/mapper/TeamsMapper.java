package ua.hospes.nfs.marathon.data.team.mapper;

import android.database.Cursor;

import com.google.common.collect.Collections2;

import java.util.List;

import ua.hospes.nfs.marathon.core.db.tables.Teams;
import ua.hospes.nfs.marathon.data.team.models.TeamDb;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
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

    public static Team map(TeamDb db, List<Driver> drivers) {
        Team team = new Team(db.getId(), db.getName());
        team.setDrivers(Collections2.transform(drivers, Driver::getName));
        return team;
    }

    public static TeamDb map(Team driver) {
        return new TeamDb(driver.getId(), driver.getName());
    }
}