package ua.hospes.rtm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.cars.toDomain
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.cars.toDbEntity

internal class CarsRepositoryImpl(private val dao: CarDAO) : CarsRepository {
    override suspend fun get(): List<Car> =
            withContext(Dispatchers.IO) { dao.get().map { it.toDomain() } }

    override suspend fun get(vararg ids: Long): List<Car> =
            withContext(Dispatchers.IO) { dao.getByIds(*ids).map { it.toDomain() } }

    override suspend fun getNotInRace(): List<Car> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listen(): Flow<List<Car>> = dao.observe().map { list -> list.map { it.toDomain() } }

    override suspend fun save(car: Car): Car =
            withContext(Dispatchers.IO) { dao.save(car.toDbEntity()).let { car.copy(id = it) } }

    override suspend fun delete(id: Long) =
            withContext(Dispatchers.IO) { dao.delete(id) }

    override suspend fun clear() =
            withContext(Dispatchers.IO) { dao.clear() }
}