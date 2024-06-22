package ua.hospes.rtm.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.model.TeamDto
import ua.hospes.rtm.data.repo.TeamsRepository
import ua.hospes.rtm.domain.team.toDomain
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val repo: TeamsRepository
) : ViewModel() {

    val teams = repo.listen().map { it.map(TeamDto::toDomain) }


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { }
}