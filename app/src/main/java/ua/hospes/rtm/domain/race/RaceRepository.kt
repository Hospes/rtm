package ua.hospes.rtm.domain.race

import kotlinx.coroutines.flow.Flow
import ua.hospes.rtm.domain.race.models.RaceItem

internal interface RaceRepository {
    fun listen(): Flow<List<RaceItem>>
    fun listen(id: Long): Flow<RaceItem>

    suspend fun save(race: RaceItem)
    suspend fun clear()
}