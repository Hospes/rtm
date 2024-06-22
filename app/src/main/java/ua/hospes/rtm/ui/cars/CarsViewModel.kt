package ua.hospes.rtm.ui.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.model.CarDto
import ua.hospes.rtm.data.repo.CarsRepository
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.toDomain
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val repo: CarsRepository
) : ViewModel() {

    val cars = repo.listen().map { it.map(CarDto::toDomain) }

    fun removeAll() = viewModelScope.launch { repo.clear() }.let { }
}