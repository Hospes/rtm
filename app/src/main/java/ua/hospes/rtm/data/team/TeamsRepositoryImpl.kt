package ua.hospes.rtm.data.team

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository

class TeamsRepositoryImpl(
        private val dbStorage: TeamsDbStorage,
        private val driversRepo: DriversRepository
) : TeamsRepository {


    override fun get(): Single<List<Team>> = dbStorage.get()
            .flatMapSingle { teamDb ->
                Single.zip<TeamDb, List<Driver>, Team>(
                        Single.just(teamDb),
                        getDriversByTeamId(teamDb.id!!),
                        BiFunction<TeamDb, List<Driver>, Team> { t1: TeamDb, t2: List<Driver> -> TeamsMapper.map(t1, t2) })
            }.toList()

    override fun get(id: Int): Single<Team> = dbStorage.get(id)
            .flatMapSingle { teamDb ->
                Single.zip<TeamDb, List<Driver>, Team>(
                        Single.just(teamDb),
                        getDriversByTeamId(teamDb.id!!),
                        BiFunction<TeamDb, List<Driver>, Team> { t1: TeamDb, t2: List<Driver> -> TeamsMapper.map(t1, t2) })
            }.firstOrError()

    override fun getNotInRace(): Single<List<Team>> = dbStorage.getNotInRace()
            .flatMapSingle { teamDb ->
                Single.zip<TeamDb, List<Driver>, Team>(
                        Single.just(teamDb),
                        getDriversByTeamId(teamDb.id!!),
                        BiFunction<TeamDb, List<Driver>, Team> { t1: TeamDb, t2: List<Driver> -> TeamsMapper.map(t1, t2) })
            }.toList()

    override fun listen(): Observable<List<Team>> = dbStorage.listen()
            .flatMapSingle { teamDbs ->
                Observable.fromIterable<TeamDb>(teamDbs)
                        .flatMapSingle { teamDb ->
                            Single.zip<TeamDb, List<Driver>, Team>(
                                    Single.just(teamDb),
                                    getDriversByTeamId(teamDb.id!!),
                                    BiFunction<TeamDb, List<Driver>, Team> { t1: TeamDb, t2: List<Driver> -> TeamsMapper.map(t1, t2) })
                        }.toList()
            }

    override fun save(team: Team): Completable = when (team.id) {
        null -> dbStorage.add(TeamsMapper.map(team))
                .flatMapCompletable { updateDriversTeam(it.result.toInt(), team.drivers) }
        else -> dbStorage.update(TeamsMapper.map(team))
                .flatMapCompletable { updateDriversTeam(it.data.id!!, team.drivers) }
    }

    override fun removeAll(): Completable = dbStorage.removeAll()
    override fun remove(id: Int): Completable = dbStorage.remove(id)


    private fun getDriversByTeamId(teamId: Int) = driversRepo.getByTeamId(teamId)

    private fun updateDriversTeam(teamId: Int, drivers: List<Driver>) =
            Observable.fromIterable(drivers).map { it.id }.toList()
                    .flatMap { removeDriversFromTeamZip(driversRepo.removeDriversFromTeam(teamId), it.filterNotNull()) }
                    .flatMapCompletable { driversRepo.addDriversToTeam(teamId, *it.toIntArray()) }

    private fun removeDriversFromTeamZip(driversOperations: Completable, driverIds: List<Int>) = Single.zip(
            driversOperations.toSingleDefault(true), Single.just(driverIds),
            BiFunction { _: Boolean, t2: List<Int> -> t2 }
    )
}