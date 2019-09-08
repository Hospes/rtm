package ua.hospes.rtm.domain.team

import kotlinx.coroutines.flow.Flow

internal interface TeamsRepository {
    suspend fun get(): List<Team>

    suspend fun get(vararg ids: Int): List<Team>

    suspend fun getNotInRace(): List<Team>


    fun listen(): Flow<List<Team>>


    suspend fun save(team: Team)

    suspend fun delete(id: Int)

    suspend fun clear()
}