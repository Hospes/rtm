package ua.hospes.rtm.ui.drivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.repo.DriversRepository
import javax.inject.Inject

@HiltViewModel
class DriversViewModel @Inject constructor(
    private val repo: DriversRepository
) : ViewModel() {

    val driversLiveData = repo.listen().asLiveData()
    val drivers = repo.listen()


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { }
}