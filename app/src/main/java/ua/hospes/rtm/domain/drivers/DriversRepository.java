package ua.hospes.rtm.domain.drivers;

import java.util.List;

import rx.Observable;
import ua.hospes.rtm.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public interface DriversRepository {
    Observable<Driver> get();

    Observable<Driver> get(int... ids);

    Observable<Driver> getByTeamId(int teamId);

    Observable<List<Driver>> listen();


    Observable<Boolean> save(Driver driver);

    Observable<Boolean> delete(Driver driver);

    Observable<Boolean> addDriversToTeam(int teamId, int... driverIds);

    Observable<Boolean> removeDriversFromTeam(int teamId);

    Observable<Void> clear();
}