package ua.hospes.rtm.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import ua.hospes.rtm.data.DriversRepository
import javax.inject.Inject

@HiltViewModel
class SelectDriversViewModel @Inject constructor(
    private val repo: DriversRepository
) : ViewModel() {

    val drivers = repo.listen()
        .map { list -> list.sortedBy { it.name }.sortedBy { it.teamName } }
        .asLiveData()


    suspend fun save(selectedIds: List<Long>) = repo.get(*selectedIds.toLongArray())
}