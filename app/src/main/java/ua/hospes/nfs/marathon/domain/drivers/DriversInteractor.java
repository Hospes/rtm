package ua.hospes.nfs.marathon.domain.drivers;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
public class DriversInteractor {
    private final DriversRepository repository;

    @Inject
    public DriversInteractor(DriversRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Driver>> listen() {
        return repository.listen();
    }
}