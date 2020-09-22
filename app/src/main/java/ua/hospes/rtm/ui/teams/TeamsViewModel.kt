package ua.hospes.rtm.ui.teams

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.TeamsRepository

class TeamsViewModel @ViewModelInject constructor(
        private val repo: TeamsRepository
) : ViewModel() {

    val teams = repo.listen().asLiveData()


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { Unit }
}