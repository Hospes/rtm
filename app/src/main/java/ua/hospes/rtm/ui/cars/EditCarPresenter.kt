package ua.hospes.rtm.ui.cars

import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

class EditCarPresenter @Inject constructor(
        private val carsRepo: CarsRepository
) : Presenter<EditCarContract.View>() {
    private val initCarSubject = BehaviorSubject.create<Car>()
    private val deleteButtonSubject = BehaviorSubject.createDefault(false)


    override fun attachView(view: EditCarContract.View?) {
        super.attachView(view)

        disposables += initCarSubject.compose(RxUtils.applySchedulers()).subscribe {
            view?.onInitCar(it)
            deleteButtonSubject.onNext(true)
        }

        disposables += deleteButtonSubject.compose(RxUtils.applySchedulers()).subscribe { view?.onDeleteButtonAvailable(it) }
    }


    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initCar(car: Car?) = car?.let { initCarSubject.onNext(it) }

    fun save(number: CharSequence?, quality: Car.Quality, broken: Boolean) = launch {
        val car = Car(
                initCarSubject.value?.id,
                number?.toString()?.toIntOrNull() ?: return@launch,
                quality,
                broken
        )

        try {
            withContext(Dispatchers.IO) { carsRepo.save(car).blockingAwait() }
        } catch (t: Throwable) {
            error(t)
            return@launch
        }

        withContext(Dispatchers.Main) { view?.onSaved() }
    }

    fun delete() = launch {
        val id = initCarSubject.value?.id ?: return@launch

        try {
            withContext(Dispatchers.IO) { carsRepo.remove(id).blockingAwait() }
        } catch (t: Throwable) {
            error(t)
            return@launch
        }

        withContext(Dispatchers.Main) { view?.onDeleted() }
    }
}