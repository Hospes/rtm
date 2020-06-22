package ua.hospes.rtm.ui.cars

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.CarsRepository
import ua.hospes.rtm.domain.cars.Car

class CarsViewModel @ViewModelInject constructor(
        private val repo: CarsRepository
) : ViewModel() {

    val cars: LiveData<List<Car>> = repo.listen().asLiveData()


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { Unit }
}