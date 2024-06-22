package ua.hospes.rtm.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import ua.hospes.rtm.data.model.DriverDto
import ua.hospes.rtm.data.repo.DriversRepository
import ua.hospes.rtm.domain.drivers.toDomain
import javax.inject.Inject

@HiltViewModel
class SelectDriversViewModel @Inject constructor(
    private val repo: DriversRepository
) : ViewModel() {

    val drivers = repo.listen().map { it.map(DriverDto::toDomain) }
        .map { list -> list.sortedBy { it.name }.sortedBy { it.teamName } }
        .asLiveData()


    suspend fun save(selectedIds: List<Long>) = repo.get(*selectedIds.toLongArray()).map(DriverDto::toDomain)
}