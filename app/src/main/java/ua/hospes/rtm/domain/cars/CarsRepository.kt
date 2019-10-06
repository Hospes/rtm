package ua.hospes.rtm.domain.cars

import kotlinx.coroutines.flow.Flow

internal interface CarsRepository {
    suspend fun get(): List<Car>

    suspend fun getNotInRace(): List<Car>


    fun listen(): Flow<List<Car>>


    suspend fun save(car: Car): Car

    suspend fun delete(id: Long)

    suspend fun clear()
}