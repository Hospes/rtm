package ua.hospes.rtm.ui.race.detail;

import javax.inject.Inject;

import ua.hospes.rtm.core.ui.BasePresenter;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
public class RaceItemDetailPresenter extends BasePresenter<RaceItemDetailContract.View> {
    private final RaceItemDetailInteractor interactor;

    @Inject
    RaceItemDetailPresenter(RaceItemDetailInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(RaceItemDetailContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.unsubscribe(this);
    }


    void listenRaceItem(int raceItemId) {
        RxUtils.manage(this, interactor.listenRaceItem(raceItemId)
                .compose(RxUtils.applySchedulers())
                .subscribe(raceItem -> {
                    getView().onUpdateRaceItem(raceItem);
                    if (raceItem == null) return;
                    listenSessions(raceItem.getTeam().getId());
                }, Throwable::printStackTrace));
    }

    private void listenSessions(int teamId) {
        RxUtils.manage(this, interactor.listenSessions(teamId)
                .compose(RxUtils.applySchedulers())
                .subscribe(sessions -> getView().onUpdateSessions(sessions), Throwable::printStackTrace));
    }
}