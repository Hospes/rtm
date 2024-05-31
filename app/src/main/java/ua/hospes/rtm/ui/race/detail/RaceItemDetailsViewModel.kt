package ua.hospes.rtm.ui.race.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ua.hospes.rtm.data.repo.RaceRepository
import ua.hospes.rtm.data.repo.SessionsRepository
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
        raceRepo.listen(raceTeamId),
        sessionRepo.listenByRaceId(raceTeamId),
        loadingState.observable
    ) { item, sessions, refreshing ->
        RaceItemDetailsViewState(
            item = item,
            sessions = sessions,
            refreshing = refreshing
        )
    }

    fun listenRaceItem() = raceRepo.listen(raceTeamId).asLiveData(viewModelScope.coroutineContext)
    fun listenSessions() = sessionRepo.listenByRaceId(raceTeamId).asLiveData(viewModelScope.coroutineContext)
}