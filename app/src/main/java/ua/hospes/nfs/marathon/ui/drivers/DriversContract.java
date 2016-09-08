package ua.hospes.nfs.marathon.ui.drivers;

import java.util.List;

import ua.hospes.nfs.marathon.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public interface DriversContract {
    interface View {
        void updateDrivers(List<Driver> drivers);
    }
}