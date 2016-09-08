package ua.hospes.nfs.marathon.data.drivers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import ua.hospes.nfs.marathon.data.drivers.mapper.DriversMapper;
import ua.hospes.nfs.marathon.data.drivers.storage.DriversDbStorage;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class DriversRepositoryImpl implements DriversRepository {
    private final DriversDbStorage dbStorage;


    @Inject
    public DriversRepositoryImpl(DriversDbStorage dbStorage) {
        this.dbStorage = dbStorage;
    }


    @Override
    public Observable<Driver> get() {
        return dbStorage.get().map(DriversMapper::map);
    }

    @Override
    public Observable<List<Driver>> listen() {
        return dbStorage.listen().map(DriversMapper::map);
    }

    @Override
    public Observable<Boolean> save(Driver driver) {
        if (driver.getId() == -1) {
            return dbStorage.add(DriversMapper.map(driver)).map(result -> result.getResult() != 0);
        } else {
            return dbStorage.update(DriversMapper.map(driver)).map(result -> result.getResult() != 0);
        }
    }

    @Override
    public Observable<Boolean> delete(Driver driver) {
        return dbStorage.remove(DriversMapper.map(driver)).map(count -> count != 0);
    }
}