package ua.hospes.rtm.ui.teams;

import javax.inject.Inject;

import ua.hospes.rtm.core.ui.BasePresenter;
import ua.hospes.rtm.domain.team.TeamsInteractor;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
class TeamsPresenter extends BasePresenter<TeamsContract.View> {
    private final TeamsInteractor interactor;

    @Inject
    TeamsPresenter(TeamsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(TeamsContract.View view) {
        super.attachView(view);

        RxUtils.manage(this, interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().updateTeams(list), Throwable::printStackTrace));
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