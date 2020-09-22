package ua.hospes.rtm.ui.cars

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.CarsRepository

class CarsViewModel @ViewModelInject constructor(
        private val repo: CarsRepository
) : ViewModel() {

    val cars = repo.listen().asLiveData()


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { Unit }
}