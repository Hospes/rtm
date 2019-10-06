package ua.hospes.rtm.domain.drivers

import kotlinx.coroutines.flow.Flow

internal interface DriversRepository {
    suspend fun get(): List<Driver>

    suspend fun get(vararg ids: Long): List<Driver>

    suspend fun getNotInRace(teamId: Long): List<Driver>


    fun listen(): Flow<List<Driver>>


    suspend fun addDriversToTeam(teamId: Long, vararg driverIds: Long)

    suspend fun removeDriversFromTeam(teamId: Long)


    suspend fun save(driver: Driver): Driver

    suspend fun delete(id: Long)

    suspend fun clear()
}