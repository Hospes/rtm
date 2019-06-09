package ua.hospes.rtm.domain.drivers

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface DriversRepository {
    fun listen(): Observable<List<Driver>>

    fun get(): Single<List<Driver>>

    operator fun get(vararg ids: Int): Single<List<Driver>>

    fun getByTeamId(teamId: Int): Single<List<Driver>>


    fun save(driver: Driver): Completable

    fun addDriversToTeam(teamId: Int, vararg driverIds: Int): Completable

    fun removeDriversFromTeam(teamId: Int): Completable


    fun remove(id: Int): Completable

    fun removeAll(): Completable
}