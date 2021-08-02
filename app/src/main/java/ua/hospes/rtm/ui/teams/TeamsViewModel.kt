package ua.hospes.rtm.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.TeamsRepository
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val repo: TeamsRepository
) : ViewModel() {

    val teams = repo.listen()


    fun removeAll() = viewModelScope.launch { repo.clear() }.let { }
}