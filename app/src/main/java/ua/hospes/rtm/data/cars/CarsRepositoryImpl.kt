package ua.hospes.rtm.data.cars

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.utils.RxUtils

class CarsRepositoryImpl(private val carsDbStorage: CarsDbStorage) : CarsRepository {

    override fun get(): Observable<Car> = carsDbStorage.get().map { CarsMapper.map(it) }

    override fun get(vararg ids: Int): Observable<Car> = carsDbStorage.get(*ids).map { CarsMapper.map(it) }

    override fun getNotInRace(): Observable<Car> = carsDbStorage.getNotInRace().map { CarsMapper.map(it) }

    override fun listen(): Observable<List<Car>> = carsDbStorage.listen()
            .map { carDbs -> RxUtils.listMap<Car, CarDb>(carDbs, Function { CarsMapper.map(it) }) }

    override fun save(car: Car): Completable = when (car.id) {
        null -> carsDbStorage.add(CarsMapper.map(car)).map { result -> result.result != 0L }.toList().ignoreElement()
        else -> carsDbStorage.update(CarsMapper.map(car)).map { result -> result.result != 0L }.toList().ignoreElement()
    }

    override fun remove(id: Int): Completable = carsDbStorage.remove(id).ignoreElement()

    override fun removeAll(): Completable = carsDbStorage.removeAll().ignoreElement()
}