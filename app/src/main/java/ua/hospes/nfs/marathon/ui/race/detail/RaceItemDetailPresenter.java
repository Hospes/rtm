package ua.hospes.nfs.marathon.ui.race.detail;

import javax.inject.Inject;

import rx.Subscription;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.core.ui.BasePresenter;
import ua.hospes.nfs.marathon.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
public class RaceItemDetailPresenter extends BasePresenter<RaceItemDetailContract.View> {
    private final RaceItemDetailInteractor interactor;

    @Inject
    public RaceItemDetailPresenter(RaceItemDetailInteractor interactor) {this.interactor = interactor;}

    @Override
    public void attachView(RaceItemDetailContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.unsubscribe(this);
    }


    public void listenRaceItem(int raceItemId) {
        Subscription subscription = interactor.listenRaceItem(raceItemId)
                .compose(RxUtils.applySchedulers())
                .subscribe(raceItem -> {
                    getView().onUpdateDetails(raceItem);
                    if (raceItem == null) return;
                    listenSessions(raceItem.getTeam().getId());
                }, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void listenSessions(int teamId) {
        Subscription subscription = interactor.listenSessions(teamId)
                .compose(RxUtils.applySchedulers())
                .subscribe(sessions -> getView().onUpdateSessions(sessions), Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }
}