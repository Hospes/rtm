package ua.hospes.rtm.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ua.hospes.rtm.data.db.AppDatabase
import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.cars.CarEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarsRepository @Inject constructor(db: AppDatabase) {
    private val dao: CarDAO = db.carDao()


    suspend fun get(): List<CarEntity> = withContext(Dispatchers.IO) { dao.get() }

    suspend fun getNotInRace(): List<CarEntity> = withContext(Dispatchers.IO) { dao.getNotSelected() }

    fun listen(): Flow<List<CarEntity>> = dao.observe()

    suspend fun save(car: CarEntity): CarEntity = withContext(Dispatchers.IO) { dao.save(car).let { car.copy(id = it) } }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) { dao.delete(id) }

    suspend fun clear() = withContext(Dispatchers.IO) { dao.clear() }
}