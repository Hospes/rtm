package ua.hospes.rtm.domain.cars;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.rtm.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public class CarsInteractor {
    private final CarsRepository repository;


    @Inject
    public CarsInteractor(CarsRepository repository) {
        this.repository = repository;
    }


    public Observable<List<Car>> listen() {
        return repository.listen();
    }

    public Observable<Void> clear() {
        return repository.clear();
    }
}