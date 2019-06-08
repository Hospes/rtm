package ua.hospes.rtm.domain.cars

import io.reactivex.Completable
import io.reactivex.Observable

interface CarsRepository {
    fun get(): Observable<Car>

    fun get(vararg ids: Int): Observable<Car>

    fun getNotInRace(): Observable<Car>

    fun listen(): Observable<List<Car>>

    fun save(car: Car): Completable

    fun remove(id: Int): Completable

    fun removeAll(): Completable
}