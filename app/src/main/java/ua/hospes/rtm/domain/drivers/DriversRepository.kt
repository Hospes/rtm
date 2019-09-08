package ua.hospes.rtm.domain.drivers

import kotlinx.coroutines.flow.Flow

internal interface DriversRepository {
    suspend fun get(): List<Driver>

    suspend fun get(vararg ids: Int): List<Driver>

    suspend fun getByTeamId(teamId: Int): List<Driver>


    fun listen(): Flow<List<Driver>>


    suspend fun addDriversToTeam(teamId: Int, vararg driverIds: Int)

    suspend fun removeDriversFromTeam(teamId: Int)


    suspend fun save(driver: Driver)

    suspend fun delete(id: Int)

    suspend fun clear()
}