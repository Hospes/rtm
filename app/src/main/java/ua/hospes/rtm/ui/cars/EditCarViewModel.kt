package ua.hospes.rtm.ui.cars

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.hospes.rtm.data.CarsRepository
import ua.hospes.rtm.domain.cars.Car
import javax.inject.Inject

@HiltViewModel
class EditCarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: CarsRepository
) : ViewModel() {

    private val carLiveData = savedStateHandle.getLiveData<Car?>("car", null)
    val car: LiveData<Car?> = carLiveData


    suspend fun save(number: Int, quality: Car.Quality, broken: Boolean) =
        repo.save(Car(carLiveData.value?.id ?: 0, number, quality, broken))

    suspend fun delete() = carLiveData.value?.id?.let { repo.delete(it) } ?: Unit
}