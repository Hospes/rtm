package ua.hospes.rtm.ui.race

import android.content.Context
import android.widget.Toast

import androidx.fragment.app.FragmentManager

import javax.inject.Inject

import ua.hospes.rtm.core.ui.BasePresenter
import ua.hospes.rtm.domain.race.RaceInteractor
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.ui.race.detail.RaceItemDetailActivity
import ua.hospes.rtm.utils.RxUtils

/**
 * @author Andrew Khloponin
 */
internal class RacePresenter @Inject
constructor(private val interactor: RaceInteractor) : BasePresenter<RaceContract.View>() {

    override fun attachView(view: RaceContract.View) {
        super.attachView(view)

        RxUtils.manage(this, interactor.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe({ list -> view.update(list) }, Consumer<Throwable> { it.printStackTrace() }))
    }

    override fun detachView() {
        super.detachView()
        RxUtils.unsubscribe(this)
    }

    fun startRace(startTime: Long) {
        RxUtils.manage(this, interactor.startRace(startTime)
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ result -> }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun stopRace(stopTime: Long) {
        RxUtils.manage(this, interactor.stopRace(stopTime)
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ result -> }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun onPit(item: RaceItem, time: Long) {
        RxUtils.manage(this, interactor.teamPit(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe({ result -> }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun onOut(item: RaceItem, time: Long) {
        RxUtils.manage(this, interactor.teamOut(item, time)
                .compose(RxUtils.applySchedulers())
                .subscribe({ result -> }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun undoLastSession(item: RaceItem) {
        RxUtils.manage(this, interactor.removeLastSession(item)
                .compose(RxUtils.applySchedulers())
                .subscribe({ result -> }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun exportXLS() {
        RxUtils.manage(this, interactor.exportXLS()
                .compose<File>(RxUtils.applySchedulersSingle<File>())
                .subscribe({ result -> Toast.makeText(view!!.getContext(), "File located at: " + result.getAbsolutePath(), Toast.LENGTH_LONG).show() }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun resetRace() {
        RxUtils.manage(this, interactor.resetRace()
                .compose<Optional>(RxUtils.applySchedulers<Optional>())
                .subscribe({ result -> }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun removeAll() {
        RxUtils.manage(this, interactor.removeAll()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ result -> }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun showRaceItemDetail(context: Context, item: RaceItem) {
        RaceItemDetailActivity.start(context, item.id)
    }

    fun showSetCarDialog(managerFragment: FragmentManager, session: Session) {
        RxUtils.manage(this, interactor.carsNotInRace
                .toList()
                .compose<List<Car>>(RxUtils.applySchedulersSingle<List<Car>>())
                .subscribe({ result -> SetCarDialogFragment.newInstance(session.id, result).show(managerFragment, "set_car") }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun showSetDriverDialog(managerFragment: FragmentManager, session: Session) {
        RxUtils.manage(this, interactor.getDrivers(session.teamId)
                .compose<List<Driver>>(RxUtils.applySchedulersSingle<List<Driver>>())
                .subscribe({ result -> SetDriverDialogFragment.newInstance(session.id, session.teamId, result).show(managerFragment, "set_driver") }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun showAddTeamDialog(managerFragment: FragmentManager) {
        AddTeamToRaceDialogFragment().show(managerFragment, "add_team_to_race")
    }
}