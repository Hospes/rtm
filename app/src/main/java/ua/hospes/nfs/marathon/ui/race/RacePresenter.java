package ua.hospes.nfs.marathon.ui.race;

import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import rx.Subscription;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.core.ui.BasePresenter;
import ua.hospes.nfs.marathon.domain.race.RaceInteractor;
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

    public void showAddTeamDialog(FragmentManager managerFragment) {
        interactor.getNotInRace()
                .compose(RxUtils.applySchedulers())
                .subscribe(teams -> {
                    AddTeamToRaceDialogFragment.newInstance(teams).show(managerFragment, "add_team_to_race");
                }, Throwable::printStackTrace);
    }
}