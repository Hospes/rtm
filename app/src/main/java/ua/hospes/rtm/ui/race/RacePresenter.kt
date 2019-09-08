package ua.hospes.rtm.ui.race

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.race.RaceInteractor
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

internal class RacePresenter @Inject constructor(
        private val interactor: RaceInteractor
) : Presenter<RaceContract.View>() {


    override fun attachView(view: RaceContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        disposables += interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe({ list -> view?.onData(list) }, this::error)
    }


    fun startRace(startTime: Long) {
        disposables += interactor.startRace(startTime)
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ }, this::error)
    }

    fun stopRace(stopTime: Long) {
        disposables += interactor.stopRace(stopTime)
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ }, this::error)
    }

    fun onPit(item: RaceItem, time: Long) {
        disposables += interactor.teamPit(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe({ }, this::error)
    }

    fun onOut(item: RaceItem, time: Long) {
        disposables += interactor.teamOut(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe({ }, this::error)
    }

    fun undoLastSession(item: RaceItem) {
        disposables += interactor.removeLastSession(item)
                .compose(RxUtils.applySchedulers())
                .subscribe({ }, this::error)
    }

    fun exportXLS() {
        disposables += interactor.exportXLS()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({
                    //result -> Toast.makeText(view!!.getContext(), "File located at: " + result.getAbsolutePath(), Toast.LENGTH_LONG).show()
                }, this::error)
    }

    fun resetRace() {
        disposables += interactor.resetRace()
                .compose(RxUtils.applySchedulers())
                .subscribe({ }, this::error)
    }

    fun removeAll() {
        disposables += interactor.removeAll()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ }, this::error)
    }

    fun showRaceItemDetail(context: Context, item: RaceItem) {
        //RaceItemDetailActivity.start(context, item.id)
    }

    fun showSetCarDialog(managerFragment: FragmentManager, session: Session) {
        disposables += interactor.carsNotInRace.toList()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ }, this::error)
        //.subscribe({ result -> SetCarDialogFragment.newInstance(session.id, result).show(managerFragment, "set_car") }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun showSetDriverDialog(managerFragment: FragmentManager, session: Session) {
        disposables += interactor.getDrivers(session.teamId)
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ }, this::error)
        //.subscribe({ result -> SetDriverDialogFragment.newInstance(session.id, session.teamId, result).show(managerFragment, "set_driver") }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun showAddTeamDialog(managerFragment: FragmentManager) {
        AddTeamToRaceDialogFragment().show(managerFragment, "add_team_to_race")
    }
}