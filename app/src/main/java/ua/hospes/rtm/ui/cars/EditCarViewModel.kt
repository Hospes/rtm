package ua.hospes.rtm.ui.cars

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.data.CarsRepository
import ua.hospes.rtm.domain.cars.Car

@Suppress("EXPERIMENTAL_API_USAGE")
class EditCarViewModel @ViewModelInject constructor(
        private val repo: CarsRepository
) : ViewModel() {
    private val initCar = ConflatedBroadcastChannel<Car>()
    private val deleteAvailable = MutableStateFlow(false)


    init {
        viewModelScope.launch { initCar.asFlow().take(1).collect { deleteAvailable.value = true } }
    }


    val init: LiveData<Car>
        get() = initCar.asFlow().asLiveData()

    val delAvailable: LiveData<Boolean>
        get() = deleteAvailable.asLiveData()


    fun initCar(car: Car?) = car?.let { initCar.offer(it) }.let { Unit }

    suspend fun save(number: CharSequence?, quality: Car.Quality, broken: Boolean) = withContext(Dispatchers.Default) {
        val car = Car(
                initCar.valueOrNull?.id ?: 0,
                number?.toString()?.toIntOrNull() ?: return@withContext,
                quality,
                broken
        )

        repo.save(car)
        Unit
    }

    suspend fun delete() = withContext(Dispatchers.Default) { repo.delete(initCar.valueOrNull?.id ?: return@withContext) }
}