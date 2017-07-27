package ua.hospes.rtm.ui.drivers;

import java.util.Collections;

import javax.inject.Inject;

import ua.hospes.rtm.core.ui.BasePresenter;
import ua.hospes.rtm.domain.drivers.DriversInteractor;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
class DriversPresenter extends BasePresenter<DriversContract.View> {
    private final DriversInteractor interactor;

    @Inject
    DriversPresenter(DriversInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(DriversContract.View view) {
        super.attachView(view);

        RxUtils.manage(this, interactor.listen()
                .map(drivers -> {
                    // Sort by Driver Name
                    Collections.sort(drivers, (driver, driver2) -> driver.getName().compareTo(driver2.getName()));
                    return drivers;
                })
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().updateDrivers(list), Throwable::printStackTrace));
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.unsubscribe(this);
    }


    public void removeAll() {
        RxUtils.manage(this, interactor.removeAll()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(result -> {}, Throwable::printStackTrace));
    }
}