package ua.hospes.rtm.data.drivers

import android.database.Cursor
import ua.hospes.rtm.core.db.tables.Drivers
import ua.hospes.rtm.data.team.TeamDb
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.utils.Optional

object DriversMapper {
    @JvmStatic fun map(cursor: Cursor): DriverDb = DriverDb(
            cursor.getInt(cursor.getColumnIndex(Drivers.ID.name())),
            cursor.getString(cursor.getColumnIndex(Drivers.NAME.name())),
            cursor.getInt(cursor.getColumnIndex(Drivers.TEAM_ID.name()))
    )

    @JvmStatic fun map(db: DriverDb, team: Optional<TeamDb>): Driver = Driver(db.id, db.name, team.get()?.id, team.get()?.name)

    @JvmStatic fun map(driver: Driver): DriverDb = DriverDb(driver.id, driver.name, driver.teamId)
}