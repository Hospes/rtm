package ua.hospes.rtm.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.base.util.AppCoroutineDispatchers
import ua.hospes.rtm.data.db.AppDatabase
import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.cars.CarEntity
import ua.hospes.rtm.data.model.CarDto
import ua.hospes.rtm.data.model.toDbEntity
import ua.hospes.rtm.data.model.toDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarsRepository @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    db: AppDatabase,
) {
    private val dispatcher = dispatchers.io
    private val dao: CarDAO = db.carDao()


    suspend fun get(): List<CarDto> = withContext(dispatcher) { dao.get().map(CarEntity::toDto) }

    suspend fun getNotInRace(): List<CarDto> = withContext(dispatcher) { dao.getNotSelected().map(CarEntity::toDto) }

    fun listen(): Flow<List<CarDto>> = dao.observe().map { it.map(CarEntity::toDto) }

    suspend fun save(car: CarDto): CarDto = withContext(dispatcher) { dao.save(car.toDbEntity()).let { car.copy(id = it) } }

    suspend fun delete(id: Long) = withContext(dispatcher) { dao.delete(id) }

    suspend fun clear() = withContext(dispatcher) { dao.clear() }
}