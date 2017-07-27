package ua.hospes.rtm.ui.cars;

import javax.inject.Inject;

import ua.hospes.rtm.core.ui.BasePresenter;
import ua.hospes.rtm.domain.cars.CarsInteractor;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
class CarsPresenter extends BasePresenter<CarsContract.View> {
    private final CarsInteractor interactor;

    @Inject
    CarsPresenter(CarsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(CarsContract.View view) {
        super.attachView(view);

        RxUtils.manage(this, interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().updateCars(list), Throwable::printStackTrace));
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