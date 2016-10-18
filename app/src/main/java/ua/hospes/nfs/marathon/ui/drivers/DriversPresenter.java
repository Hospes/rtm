package ua.hospes.nfs.marathon.ui.drivers;

import javax.inject.Inject;

import rx.Subscription;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.core.ui.BasePresenter;
import ua.hospes.nfs.marathon.domain.drivers.DriversInteractor;
import ua.hospes.nfs.marathon.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
public class DriversPresenter extends BasePresenter<DriversContract.View> {
    private final DriversInteractor interactor;

    @Inject
    public DriversPresenter(DriversInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(DriversContract.View view) {
        super.attachView(view);

        Subscription subscription = interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().updateDrivers(list), Throwable::printStackTrace);
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