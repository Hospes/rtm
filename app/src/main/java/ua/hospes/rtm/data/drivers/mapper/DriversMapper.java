package ua.hospes.rtm.data.drivers.mapper;

import android.database.Cursor;
import android.support.annotation.NonNull;

import ua.hospes.rtm.core.db.tables.Drivers;
import ua.hospes.rtm.data.drivers.models.DriverDb;
import ua.hospes.rtm.data.team.models.TeamDb;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.utils.Optional;

/**
 * @author Andrew Khloponin
 */
public class DriversMapper {
    public static DriverDb map(Cursor cursor) {
        DriverDb db = new DriverDb(cursor.getInt(cursor.getColumnIndex(Drivers.ID.name())));
        db.setName(cursor.getString(cursor.getColumnIndex(Drivers.NAME.name())));
        db.setTeamId(cursor.getInt(cursor.getColumnIndex(Drivers.TEAM_ID.name())));
        return db;
    }

    @SuppressWarnings("ConstantConditions")
    public static Driver map(DriverDb db, @NonNull Optional<TeamDb> team) {
        Driver driver = new Driver(db.getId(), db.getName());
        driver.setTeamId(team.isPresent() ? team.get().getId() : -1);
        driver.setTeamName(team.isPresent() ? team.get().getName() : null);
        return driver;
    }

    public static DriverDb map(Driver driver) {
        DriverDb db = new DriverDb(driver.getId());
        db.setName(driver.getName());
        db.setTeamId(driver.getTeamId());
        return db;
    }
}