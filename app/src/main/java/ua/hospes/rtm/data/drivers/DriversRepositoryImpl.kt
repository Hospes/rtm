package ua.hospes.rtm.data.drivers

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ua.hospes.rtm.data.team.TeamDb
import ua.hospes.rtm.data.team.TeamsDbStorage
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.utils.Optional

class DriversRepositoryImpl(
        private val dbStorage: DriversDbStorage,
        private val teamsDbStorage: TeamsDbStorage
) : DriversRepository {

    override fun listen(): Observable<List<Driver>> = dbStorage.listen().flatMapSingle { this.transform(it) }

    override fun get(): Single<List<Driver>> = dbStorage.get().flatMapSingle { this.transform(it) }.toList()
    override fun get(vararg ids: Int): Single<List<Driver>> = dbStorage.get(*ids).flatMapSingle { this.transform(it) }.toList()
    override fun getByTeamId(teamId: Int): Single<List<Driver>> = dbStorage.getTeamById(teamId).flatMapSingle { this.transform(it) }.toList()


    override fun save(driver: Driver): Completable = when (driver.id) {
        null -> dbStorage.add(DriversMapper.map(driver)).map { it.result != 0L }.toList().ignoreElement()
        else -> dbStorage.update(DriversMapper.map(driver)).map { it.result != 0L }.toList().ignoreElement()
    }


    override fun addDriversToTeam(teamId: Int, vararg driverIds: Int): Completable = dbStorage.addDriversToTeam(teamId, *driverIds)

    override fun removeDriversFromTeam(teamId: Int): Completable = dbStorage.removeDriversFromTeam(teamId)
    override fun removeAll(): Completable = dbStorage.removeAll()
    override fun remove(id: Int): Completable = dbStorage.remove(id)


    private fun transform(driverDb: DriverDb) = Single.zip<DriverDb, Optional<TeamDb>, Driver>(
            Single.just(driverDb), getTeamById(driverDb.teamId!!),
            BiFunction<DriverDb, Optional<TeamDb>, Driver> { db, team -> DriversMapper.map(db, team) }
    )

    private fun transform(driverDbs: List<DriverDb>) = Observable.fromIterable(driverDbs)
            .flatMapSingle { transform(it) }.toList()

    private fun getTeamById(id: Int): Single<Optional<TeamDb>> =
            teamsDbStorage.get(id).map<Optional<TeamDb>> { Optional.of(it) }.single(Optional.empty())

}