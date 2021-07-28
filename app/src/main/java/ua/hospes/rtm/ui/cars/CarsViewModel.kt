package ua.hospes.rtm.ui.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.CarsRepository
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val repo: CarsRepository
) : ViewModel() {

    val cars = repo.listen().asLiveData()


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { Unit }
}