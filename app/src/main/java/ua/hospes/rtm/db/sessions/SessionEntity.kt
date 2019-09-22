package ua.hospes.rtm.db.sessions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.cars.toDomain
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.drivers.toDomain
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.sessions.Session

@Entity(tableName = "sessions")
data class SessionEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = "team_id") val teamId: Long,
        @ColumnInfo(name = "driver_id") val driverId: Long? = null,
        @ColumnInfo(name = "car_id") val carId: Long? = null,
        @ColumnInfo(name = "race_start_time") val raceStartTime: Long? = null,
        @ColumnInfo(name = "start_duration_time") val startDurationTime: Long? = null,
        @ColumnInfo(name = "end_duration_time") val endDurationTime: Long? = null,
        @ColumnInfo(name = "type") val type: String
)

internal suspend fun SessionEntity.toDomain(teamDAO: TeamDAO, driverDAO: DriverDAO, carDAO: CarDAO): Session = withContext(Dispatchers.IO) {
    Session(
            id = id,
            teamId = teamId,
            driver = driverId?.let { driverDAO.get(it).toDomain(teamDAO) },
            car = carId?.let { carDAO.get(it).toDomain() },
            raceStartTime = raceStartTime,
            startDurationTime = startDurationTime,
            endDurationTime = endDurationTime,
            type = Session.Type.valueOf(type)
    )
}