package ua.hospes.rtm.ui.race.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ua.hospes.rtm.data.model.RaceDto
import ua.hospes.rtm.data.model.SessionDto
import ua.hospes.rtm.data.repo.RaceRepository
import ua.hospes.rtm.data.repo.SessionsRepository
import ua.hospes.rtm.domain.race.models.toDomain
import ua.hospes.rtm.domain.sessions.toDomain
import ua.hospes.rtm.utils.ObservableLoadingCounter
import javax.inject.Inject

@HiltViewModel
class RaceItemDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionRepo: SessionsRepository,
    private val raceRepo: RaceRepository
) : ViewModel() {
    private val raceTeamId = savedStateHandle.get<Long>("race_team_id")!!
    private val loadingState = ObservableLoadingCounter()

    val state: Flow<RaceItemDetailsViewState> = combine(
        raceRepo.listen(raceTeamId).map(RaceDto::toDomain),
        sessionRepo.listenByRaceId(raceTeamId).map { it.map(SessionDto::toDomain) },
        loadingState.observable
    ) { item, sessions, refreshing ->
        RaceItemDetailsViewState(
            item = item,
            sessions = sessions,
            refreshing = refreshing
        )
    }

    fun listenRaceItem() = raceRepo.listen(raceTeamId).map(RaceDto::toDomain).asLiveData(viewModelScope.coroutineContext)
    fun listenSessions() = sessionRepo.listenByRaceId(raceTeamId).map { it.map(SessionDto::toDomain) }.asLiveData(viewModelScope.coroutineContext)
}