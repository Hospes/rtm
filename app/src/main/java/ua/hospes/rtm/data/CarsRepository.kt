package ua.hospes.rtm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.AppDatabase
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.cars.toDomain
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.toDbEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarsRepository @Inject constructor(db: AppDatabase) {
    private val dao: CarDAO = db.carDao()


    suspend fun get(): List<Car> = withContext(Dispatchers.IO) { dao.get().map { it.toDomain() } }

    suspend fun getNotInRace(): List<Car> = withContext(Dispatchers.IO) { dao.getNotSelected().map { it.toDomain() } }

    fun listen(): Flow<List<Car>> = dao.observe().map { list -> list.map { it.toDomain() } }

    suspend fun save(car: Car): Car = withContext(Dispatchers.IO) { dao.save(car.toDbEntity()).let { car.copy(id = it) } }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) { dao.delete(id) }

    suspend fun clear() = withContext(Dispatchers.IO) { dao.clear() }
}