package ua.hospes.rtm.data.drivers

import android.content.ContentValues
import ua.hospes.dbhelper.IDbModel
import ua.hospes.rtm.core.db.tables.Drivers

data class DriverDb(
        val id: Int? = null,
        val name: String,
        val teamId: Int? = null
) : IDbModel {
    override fun toContentValues(): ContentValues = ContentValues().apply {
        put(Drivers.NAME.name(), name)
        put(Drivers.TEAM_ID.name(), teamId)
    }
}