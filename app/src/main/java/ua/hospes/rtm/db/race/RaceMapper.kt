package ua.hospes.rtm.db.race

import android.database.Cursor

import ua.hospes.rtm.db.tables.Race
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.utils.Optional

object RaceMapper {
    fun map(cursor: Cursor): RaceItemDb {
        val teamId = cursor.getInt(cursor.getColumnIndex(Race.TEAM_ID.name()))
        val result = RaceItemDb(teamId)
        result.id = cursor.getInt(cursor.getColumnIndex(Race.ID.name()))
        result.teamNumber = cursor.getInt(cursor.getColumnIndex(Race.TEAM_NUMBER.name()))
        result.sessionId = cursor.getInt(cursor.getColumnIndex(Race.SESSION_ID.name()))
        result.order = cursor.getInt(cursor.getColumnIndex(Race.ORDER.name()))
        return result
    }

    fun map(db: RaceItemDb, team: Optional<Team>, session: Optional<Session>): RaceItem {
        val result = RaceItem(db.id, team.get()!!)
        result.teamNumber = db.teamNumber
        result.session = session.get()
        return result
    }

    fun map(item: RaceItem): RaceItemDb {
        val db = RaceItemDb(item.team.id!!)

        db.id = item.id
        db.teamNumber = item.teamNumber
        if (item.session != null)
            db.sessionId = item.session?.id!!

        return db
    }
}