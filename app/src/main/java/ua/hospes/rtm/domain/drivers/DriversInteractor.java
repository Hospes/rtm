package ua.hospes.rtm.domain.drivers;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.rtm.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public class DriversInteractor {
    private final DriversRepository repository;

    @Inject
    public DriversInteractor(DriversRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Driver>> listen() {
        return repository.listen();
    }


    public Observable<Void> clear() {
        return repository.clear();
    }
}