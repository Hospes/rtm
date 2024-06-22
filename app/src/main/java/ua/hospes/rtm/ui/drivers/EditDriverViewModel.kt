package ua.hospes.rtm.ui.drivers

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.data.model.TeamDto
import ua.hospes.rtm.data.repo.DriversRepository
import ua.hospes.rtm.data.repo.TeamsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.toDto
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.toDomain
import javax.inject.Inject

@HiltViewModel
class EditDriverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val driversRepo: DriversRepository,
    private val teamsRepo: TeamsRepository
) : ViewModel() {

    private val driverLiveData = savedStateHandle.getLiveData<Driver?>("driver", null)
    val driver: LiveData<Driver?> = driverLiveData

    val teams = teamsRepo.listen().map { it.map(TeamDto::toDomain) }.asLiveData()


    suspend fun getDriverTeamIndex() =
        withContext(Dispatchers.Default) { teamsRepo.get().indexOfFirst { it.id == driverLiveData.value?.teamId } }

    suspend fun save(name: String, team: Team? = null) =
        driversRepo.save(Driver(driverLiveData.value?.id ?: 0, name, team?.id, team?.name).toDto())

    suspend fun delete() = driverLiveData.value?.id?.let { driversRepo.delete(it) } ?: Unit
}