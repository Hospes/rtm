package ua.hospes.nfs.marathon.data.drivers.mapper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ua.hospes.nfs.marathon.core.db.tables.Drivers;
import ua.hospes.nfs.marathon.data.drivers.models.DriverDb;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public class DriversMapper {
    public static DriverDb map(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Drivers._ID));
        String name = cursor.getString(cursor.getColumnIndex(Drivers.NAME));
        int teamId = cursor.getInt(cursor.getColumnIndex(Drivers.TEAM_ID));
        return new DriverDb(id, name, teamId);
    }

    public static Driver map(DriverDb db) {
        return new Driver(db.getId(), db.getName(), db.getTeamId());
    }

    public static List<Driver> map(List<DriverDb> dbs) {
        List<Driver> result = new ArrayList<>();
        for (DriverDb db : dbs) result.add(DriversMapper.map(db));
        return result;
    }


    public static DriverDb map(Driver driver) {
        return new DriverDb(driver.getId(), driver.getName(), driver.getTeamId());
    }
}