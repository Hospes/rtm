package ua.hospes.nfs.marathon.domain.cars;

import java.util.List;

import rx.Observable;
import ua.hospes.nfs.marathon.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public interface CarsRepository {
    Observable<Car> get();

    Observable<Car> getByIds(int... ids);

    Observable<Car> getByNumbers(int... numbers);

    Observable<Car> getNotInRace();

    Observable<List<Car>> listen();

    Observable<Boolean> save(Car car);

    Observable<Boolean> delete(Car car);

    Observable<Void> clear();
}