package ua.hospes.rtm.ui.race.detail

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import ua.hospes.rtm.data.RaceRepository
import ua.hospes.rtm.data.SessionsRepository
import javax.inject.Inject

@HiltViewModel
class RaceItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionRepo: SessionsRepository,
    private val raceRepo: RaceRepository
) : ViewModel() {
    private val _raceTeamId = savedStateHandle.getLiveData<Long>("race_team_id")

    fun listenRaceItem() = _raceTeamId.asFlow().filterNotNull().flatMapLatest { raceRepo.listen(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun listenSessions() = _raceTeamId.asFlow().filterNotNull().flatMapLatest { sessionRepo.listenByRaceId(it) }
        .asLiveData(viewModelScope.coroutineContext)
}