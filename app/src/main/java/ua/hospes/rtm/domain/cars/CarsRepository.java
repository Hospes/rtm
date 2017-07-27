package ua.hospes.rtm.domain.cars;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.hospes.rtm.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public interface CarsRepository {
    Observable<Car> get();

    Observable<Car> get(int... ids);

    Observable<Car> getByNumbers(int... numbers);

    Observable<Car> getNotInRace();

    Observable<List<Car>> listen();

    Observable<Boolean> save(Car car);

    Single<Integer> remove(Car car);

    Single<Integer> removeAll();
}