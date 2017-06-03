package ua.hospes.rtm.data.cars;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import ua.hospes.rtm.data.cars.mapper.CarsMapper;
import ua.hospes.rtm.data.cars.storage.CarsDbStorage;
import ua.hospes.rtm.domain.cars.CarsRepository;
import ua.hospes.rtm.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class CarsRepositoryImpl implements CarsRepository {
    private final CarsDbStorage carsDbStorage;


    @Inject
    public CarsRepositoryImpl(CarsDbStorage carsDbStorage) {
        this.carsDbStorage = carsDbStorage;
    }


    @Override
    public Observable<Car> get() {
        return carsDbStorage.get().map(CarsMapper::map);
    }

    @Override
    public Observable<Car> getByIds(int... ids) {
        return carsDbStorage.getByIds(ids).map(CarsMapper::map);
    }

    @Override
    public Observable<Car> getByNumbers(int... numbers) {
        return carsDbStorage.getByNumbers(numbers).map(CarsMapper::map);
    }

    @Override
    public Observable<Car> getNotInRace() {
        return carsDbStorage.getNotInRace().map(CarsMapper::map);
    }

    @Override
    public Observable<List<Car>> listen() {
        return carsDbStorage.listen().flatMap(carDbs -> Observable.from(carDbs).map(CarsMapper::map).toList());
    }

    @Override
    public Observable<Boolean> save(Car car) {
        if (car.getId() == -1) {
            return carsDbStorage.add(CarsMapper.map(car)).map(result -> result.getResult() != 0);
        } else {
            return carsDbStorage.update(CarsMapper.map(car)).map(result -> result.getResult() != 0);
        }
    }

    @Override
    public Observable<Boolean> delete(Car car) {
        return carsDbStorage.remove(CarsMapper.map(car)).map(count -> count != 0).toObservable();
    }

    @Override
    public Observable<Void> clear() {
        return carsDbStorage.clear().toObservable();
    }
}