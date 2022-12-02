package ua.hospes.rtm.ui.teams

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.hospes.rtm.data.TeamsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import javax.inject.Inject

@HiltViewModel
class EditTeamViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: TeamsRepository
) : ViewModel() {

    private val teamData = savedStateHandle.getLiveData<Team?>("team", null)
    val team: LiveData<Team?> = teamData
    private val driversData = savedStateHandle.getLiveData<List<Driver>>("drivers", emptyList())
    val drivers: LiveData<List<Driver>?> = driversData


    fun initTeam(team: Team?) = with(teamData) { value = team }.also { onDriversSelected(team?.drivers) }

    fun onDriversSelected(list: List<Driver>?) = with(driversData) { value = list ?: emptyList() }

    fun getSelectedDrivers() = driversData.value ?: emptyList()


    suspend fun save(name: String) = repo.save(Team(teamData.value?.id ?: 0, name, driversData.value ?: emptyList()))
    suspend fun delete() = teamData.value?.id?.let { repo.delete(it) } ?: Unit
}