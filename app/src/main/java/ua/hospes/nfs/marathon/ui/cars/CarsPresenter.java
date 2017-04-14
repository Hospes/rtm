package ua.hospes.nfs.marathon.ui.cars;

import javax.inject.Inject;

import rx.Subscription;
import ua.hospes.nfs.marathon.core.ui.BasePresenter;
import ua.hospes.nfs.marathon.domain.cars.CarsInteractor;
import ua.hospes.nfs.marathon.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
public class CarsPresenter extends BasePresenter<CarsContract.View> {
    private final CarsInteractor interactor;

    @Inject
    public CarsPresenter(CarsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(CarsContract.View view) {
        super.attachView(view);

        Subscription subscription = interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().updateCars(list), Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.unsubscribe(this);
    }


    public void clear() {
        Subscription subscription = interactor.clear()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }
}