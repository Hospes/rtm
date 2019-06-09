package ua.hospes.rtm.ui.race;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;

import ua.hospes.rtm.core.ui.BasePresenter;
import ua.hospes.rtm.domain.race.RaceInteractor;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.Session;
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

        RxUtils.manage(this, interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe(list -> getView().update(list), Throwable::printStackTrace));
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.unsubscribe(this);
    }

    void startRace(long startTime) {
        RxUtils.manage(this, interactor.startRace(startTime)
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(result -> {}, Throwable::printStackTrace));
    }

    void stopRace(long stopTime) {
        RxUtils.manage(this, interactor.stopRace(stopTime)
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(result -> {}, Throwable::printStackTrace));
    }

    void onPit(RaceItem item, long time) {
        RxUtils.manage(this, interactor.teamPit(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace));
    }

    void onOut(RaceItem item, long time) {
        RxUtils.manage(this, interactor.teamOut(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace));
    }

    void undoLastSession(RaceItem item) {
        RxUtils.manage(this, interactor.removeLastSession(item)
                .compose(RxUtils.applySchedulers())
                .subscribe(result -> {}, Throwable::printStackTrace));
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
        RxUtils.manage(this, interactor.getCarsNotInRace()
                .toList()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(result -> {
                    SetCarDialogFragment.newInstance(session.getId(), result).show(managerFragment, "set_car");
                }, Throwable::printStackTrace));
    }

    void showSetDriverDialog(FragmentManager managerFragment, Session session) {
        RxUtils.manage(this, interactor.getDrivers(session.getTeamId())
                .compose(RxUtils.applySchedulersSingle())
                .subscribe(result -> {
                    SetDriverDialogFragment.newInstance(session.getId(), session.getTeamId(), result).show(managerFragment, "set_driver");
                }, Throwable::printStackTrace));
    }

    void showAddTeamDialog(FragmentManager managerFragment) {
        AddTeamToRaceDialogFragment.newInstance().show(managerFragment, "add_team_to_race");
    }
}