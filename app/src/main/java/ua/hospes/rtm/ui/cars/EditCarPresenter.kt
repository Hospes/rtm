package ua.hospes.rtm.ui.cars

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.CarsRepository
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class EditCarPresenter @Inject constructor(private val repo: CarsRepository) : Presenter<EditCarContract.View>() {
    private val initCar = ConflatedBroadcastChannel<Car>()
    private val deleteAvailable = ConflatedBroadcastChannel(false)


    override fun attachView(view: EditCarContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch(Dispatchers.Main) {
            initCar.consumeEach {
                view?.onInitCar(it)
                deleteAvailable.offer(true)
            }
        }
        launch(Dispatchers.Main) { deleteAvailable.consumeEach { view?.onDeleteButtonAvailable(it) } }
    }


    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initCar(car: Car?) = car?.let { initCar.offer(it) }.let { Unit }

    fun save(number: CharSequence?, quality: Car.Quality, broken: Boolean) = launch {
        val car = Car(
                initCar.valueOrNull?.id ?: 0,
                number?.toString()?.toIntOrNull() ?: return@launch,
                quality,
                broken
        )

        repo.save(car)

        withContext(Dispatchers.Main) { view?.onSaved() }
    }

    fun delete() = launch {
        repo.delete(initCar.valueOrNull?.id ?: return@launch)

        withContext(Dispatchers.Main) { view?.onDeleted() }
    }
}