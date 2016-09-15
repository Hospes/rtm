package ua.hospes.nfs.marathon.ui.cars;

import java.util.List;

import ua.hospes.nfs.marathon.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public interface CarsContract {
    interface View {
        void updateCars(List<Car> cars);
    }
}