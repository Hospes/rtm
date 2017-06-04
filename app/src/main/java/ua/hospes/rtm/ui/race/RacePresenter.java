package ua.hospes.rtm.ui.race;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import javax.inject.Inject;

import rx.Subscription;
import ua.hospes.rtm.core.ui.BasePresenter;
import ua.hospes.rtm.domain.race.RaceInteractor;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.ui.race.detail.RaceItemDetailActivity;
import ua.hospes.rtm.utils.RxUtils;

/**
 * @author Andrew Khloponin
 */
class RacePresenter extends BasePresenter<RaceContract.View> {
    private final RaceInteractor interactor;

    @Inject
    RacePresenter(RaceInteractor interactor) {
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

    void startRace(long startTime) {
        Subscription subscription = interactor.startRace(startTime)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    void stopRace(long stopTime) {
        Subscription subscription = interactor.stopRace(stopTime)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    void initSession(int teamId) {
        Subscription subscription = interactor.initSession(Session.Type.TRACK, teamId)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    void onPit(RaceItem item, long time) {
        Subscription subscription = interactor.teamPit(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    void onOut(RaceItem item, long time) {
        Subscription subscription = interactor.teamOut(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    void exportXLS() {
        RxUtils.manage(this, interactor.exportXLS()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(result -> {
                    Toast.makeText(getView().getContext(), "File located at: " + result.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }, Throwable::printStackTrace));
    }

    void resetRace() {
        RxUtils.manage(this, interactor.resetRace()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace));
    }

    void removeAll() {
        RxUtils.manage(this, interactor.removeAll()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(result -> {}, Throwable::printStackTrace));
    }

    void showRaceItemDetail(Context context, RaceItem item) {
        RaceItemDetailActivity.start(context, item.getId());
    }

    void showSetCarDialog(FragmentManager managerFragment, Session session) {
        Subscription subscription = interactor.getCarsNotInRace()
                .toList()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {
                    SetCarDialogFragment.newInstance(session.getId(), result).show(managerFragment, "set_car");
                }, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    void showSetDriverDialog(FragmentManager managerFragment, Session session) {
        Subscription subscription = interactor.getDrivers(session.getTeamId())
                .toList()
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {
                    SetDriverDialogFragment.newInstance(session.getId(), session.getTeamId(), result).show(managerFragment, "set_driver");
                }, Throwable::printStackTrace);
        RxUtils.manage(this, subscription);
    }

    void showAddTeamDialog(FragmentManager managerFragment) {
        AddTeamToRaceDialogFragment.newInstance().show(managerFragment, "add_team_to_race");
    }
}