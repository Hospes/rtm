package ua.hospes.rtm.domain.race

import kotlinx.coroutines.flow.Flow
import ua.hospes.rtm.domain.race.models.RaceItem

internal interface RaceRepository {
    suspend fun get(): List<RaceItem>

    fun listen(): Flow<List<RaceItem>>
    fun listen(id: Long): Flow<RaceItem>

    suspend fun save(race: RaceItem)
    suspend fun save(vararg races: RaceItem)

    suspend fun reset()

    suspend fun delete(item: RaceItem)
    suspend fun clear()
}