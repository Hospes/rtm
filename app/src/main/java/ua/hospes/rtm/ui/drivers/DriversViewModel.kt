package ua.hospes.rtm.ui.drivers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.DriversRepository

class DriversViewModel @ViewModelInject constructor(
        private val repo: DriversRepository
) : ViewModel() {

    val drivers = repo.listen().asLiveData()


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { Unit }
}