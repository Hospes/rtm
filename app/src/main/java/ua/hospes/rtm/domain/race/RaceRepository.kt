package ua.hospes.rtm.domain.race

import android.content.ContentValues
import android.util.Pair

import io.reactivex.Observable
import io.reactivex.Single
import ua.hospes.rtm.domain.race.models.RaceItem

/**
 * @author Andrew Khloponin
 */
interface RaceRepository {
    fun get(): Observable<RaceItem>

    fun listen(): Observable<List<RaceItem>>

    fun listen(id: Int): Observable<RaceItem>

    fun addNew(vararg items: RaceItem): Observable<Boolean>

    fun update(items: List<RaceItem>): Observable<Boolean>

    fun updateByTeamId(items: Iterable<Pair<Int, ContentValues>>): Observable<Boolean>

    fun remove(item: RaceItem): Single<Int>

    fun reset(): Observable<Void>

    fun removeAll(): Single<Int>
}