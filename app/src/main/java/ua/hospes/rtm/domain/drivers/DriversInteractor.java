package ua.hospes.rtm.domain.drivers;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import ua.hospes.rtm.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public class DriversInteractor {
    private final DriversRepository repository;

    @Inject
    DriversInteractor(DriversRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Driver>> listen() {
        return repository.listen();
    }


    public Single<Integer> removeAll() {
        return repository.removeAll();
    }
}