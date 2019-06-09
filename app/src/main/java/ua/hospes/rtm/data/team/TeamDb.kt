package ua.hospes.rtm.data.team

import android.content.ContentValues
import ua.hospes.dbhelper.IDbModel
import ua.hospes.rtm.core.db.tables.Teams

data class TeamDb(
        val id: Int? = null,
        val name: String
) : IDbModel {

    override fun toContentValues(): ContentValues = ContentValues().apply { put(Teams.NAME.name(), name) }
}