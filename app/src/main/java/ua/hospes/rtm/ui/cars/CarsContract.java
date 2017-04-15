package ua.hospes.rtm.ui.cars;

import java.util.List;

import ua.hospes.rtm.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public interface CarsContract {
    interface View {
        void updateCars(List<Car> cars);
    }
}