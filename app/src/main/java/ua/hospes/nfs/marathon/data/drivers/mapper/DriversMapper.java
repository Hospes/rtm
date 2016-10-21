package ua.hospes.nfs.marathon.data.drivers.mapper;

import android.database.Cursor;
import android.support.annotation.Nullable;

import ua.hospes.nfs.marathon.core.db.tables.Drivers;
import ua.hospes.nfs.marathon.data.drivers.models.DriverDb;
import ua.hospes.nfs.marathon.data.team.models.TeamDb;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public class DriversMapper {
    public static DriverDb map(Cursor cursor) {
        DriverDb db = new DriverDb(cursor.getInt(cursor.getColumnIndex(Drivers._ID)));
        db.setName(cursor.getString(cursor.getColumnIndex(Drivers.NAME)));
        db.setTeamId(cursor.getInt(cursor.getColumnIndex(Drivers.TEAM_ID)));
        return db;
    }

    public static Driver map(DriverDb db, @Nullable TeamDb team) {
        Driver driver = new Driver(db.getId(), db.getName());
        driver.setTeamId(team == null ? -1 : team.getId());
        driver.setTeamName(team == null ? null : team.getName());
        return driver;
    }

    public static DriverDb map(Driver driver) {
        DriverDb db = new DriverDb(driver.getId());
        db.setName(driver.getName());
        db.setTeamId(driver.getTeamId());
        return db;
    }
}