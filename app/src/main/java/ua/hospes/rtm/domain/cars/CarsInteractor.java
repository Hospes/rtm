package ua.hospes.rtm.domain.cars;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.hospes.rtm.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public class CarsInteractor {
    private final CarsRepository repository;


    @Inject
    CarsInteractor(CarsRepository repository) {
        this.repository = repository;
    }


    public Observable<List<Car>> listen() {
        return repository.listen();
    }

    public Single<Integer> removeAll() {
        return repository.removeAll();
    }
}