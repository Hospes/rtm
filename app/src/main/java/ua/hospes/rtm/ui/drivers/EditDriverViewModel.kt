package ua.hospes.rtm.ui.drivers

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.hospes.rtm.data.DriversRepository
import ua.hospes.rtm.data.TeamsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import javax.inject.Inject

@HiltViewModel
class EditDriverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val driversRepo: DriversRepository,
    private val teamsRepo: TeamsRepository
) : ViewModel() {

    private val driverLiveData = savedStateHandle.getLiveData<Driver>("driver", null)
    val driver: LiveData<Driver?> = driverLiveData

    val teams = teamsRepo.listen().asLiveData()


    fun initDriver(driver: Driver?) = with(driverLiveData) { value = driver }

    suspend fun getDriverTeamIndex() =
        withContext(Dispatchers.Default) { teamsRepo.get().indexOfFirst { it.id == driverLiveData.value?.teamId } }

    suspend fun save(name: String, team: Team? = null) =
        driversRepo.save(Driver(driverLiveData.value?.id ?: 0, name, team?.id, team?.name))

    suspend fun delete() = driverLiveData.value?.id?.let { driversRepo.delete(it) } ?: Unit
}