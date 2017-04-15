package ua.hospes.rtm.ui.drivers;

import java.util.List;

import ua.hospes.rtm.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public interface DriversContract {
    interface View {
        void updateDrivers(List<Driver> drivers);
    }
}