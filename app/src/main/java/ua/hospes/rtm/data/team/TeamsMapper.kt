package ua.hospes.rtm.data.team

import android.database.Cursor
import ua.hospes.rtm.core.db.tables.Teams
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team

object TeamsMapper {
    @JvmStatic fun map(cursor: Cursor): TeamDb = TeamDb(
            cursor.getInt(cursor.getColumnIndex(Teams.ID.name())),
            cursor.getString(cursor.getColumnIndex(Teams.NAME.name()))
    )

    @JvmStatic fun map(db: TeamDb, drivers: List<Driver>): Team = Team(db.id, db.name, drivers.toMutableList())

    @JvmStatic fun map(team: Team): TeamDb = TeamDb(team.id, team.name)
}