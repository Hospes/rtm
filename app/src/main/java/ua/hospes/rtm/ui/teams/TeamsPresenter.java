package ua.hospes.rtm.ui.teams;

import javax.inject.Inject;

import rx.Subscription;
import ua.hospes.rtm.core.ui.BasePresenter;
import ua.hospes.rtm.domain.team.TeamsInteractor;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
public class TeamsPresenter extends BasePresenter<TeamsContract.View> {
    private final TeamsInteractor interactor;

    @Inject
    public TeamsPresenter(TeamsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(TeamsContract.View view) {
        super.attachView(view);

        Subscription subscription = interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().updateTeams(list), Throwable::printStackTrace);
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