package ua.hospes.nfs.marathon.ui.race;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import rx.Subscription;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.core.ui.BasePresenter;
import ua.hospes.nfs.marathon.domain.race.RaceInteractor;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.ui.race.detail.RaceItemDetailActivity;
import ua.hospes.nfs.marathon.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
public class RacePresenter extends BasePresenter<RaceContract.View> {
    private final RaceInteractor interactor;

    @Inject
    public RacePresenter(RaceInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(RaceContract.View view) {
        super.attachView(view);

        Subscription subscription = interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().update(list), Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.unsubscribe(this);
    }

    public void startRace(long startTime) {
        Subscription subscription = interactor.startRace(startTime)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void stopRace(long stopTime) {
        Subscription subscription = interactor.stopRace(stopTime)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void initSession(int teamId) {
        Subscription subscription = interactor.initSession(Session.Type.TRACK, teamId)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void onPit(RaceItem item, long time) {
        Subscription subscription = interactor.teamPit(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void onOut(RaceItem item, long time) {
        Subscription subscription = interactor.teamOut(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void resetRace() {
        Subscription subscription = interactor.resetRace()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void clear() {
        Subscription subscription = interactor.clear()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void showRaceItemDetail(Context context, RaceItem item) {
        RaceItemDetailActivity.start(context, item.getId());
    }

    public void showSetCarDialog(FragmentManager managerFragment, Session session) {
        Subscription subscription = interactor.getCarsNotInRace()
                .toList()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {
                    SetCarDialog.newInstance(session.getId(), result).show(managerFragment, "set_car");
                }, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void showSetDriverDialog(FragmentManager managerFragment, Session session) {
        Subscription subscription = interactor.getDrivers(session.getTeamId())
                .toList()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {
                    SetDriverDialog.newInstance(session.getId(), session.getTeamId(), result).show(managerFragment, "set_driver");
                }, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    public void showAddTeamDialog(FragmentManager managerFragment) {
        AddTeamToRaceDialogFragment.newInstance().show(managerFragment, "add_team_to_race");
    }
}