package ua.hospes.rtm.ui.race.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import ua.hospes.rtm.data.RaceRepository
import ua.hospes.rtm.data.SessionsRepository
import javax.inject.Inject

@HiltViewModel
class RaceItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionRepo: SessionsRepository,
    private val raceRepo: RaceRepository
) : ViewModel() {
    private val idLiveData = savedStateHandle.getLiveData<Long>("race_item_id")

    fun init(id: Long) = with(idLiveData) { value = id }

    fun listenRaceItem() = idLiveData.asFlow().filterNotNull().flatMapConcat { raceRepo.listen(it) }.asLiveData()
    fun listenSessions() = idLiveData.asFlow().filterNotNull().flatMapConcat { sessionRepo.listenByRaceId(it) }.asLiveData()
}