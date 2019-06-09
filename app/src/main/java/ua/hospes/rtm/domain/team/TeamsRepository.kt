package ua.hospes.rtm.domain.team

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface TeamsRepository {

    fun listen(): Observable<List<Team>>


    fun get(): Single<List<Team>>

    fun get(id: Int): Single<Team>

    /**
     * Get teams that not yet in Race
     */
    fun getNotInRace(): Single<List<Team>>


    fun save(team: Team): Completable

    fun remove(id: Int): Completable

    fun removeAll(): Completable
}