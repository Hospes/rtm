package ua.hospes.rtm.ui.race.detail

import androidx.compose.runtime.Immutable
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session

@Immutable
data class RaceItemDetailsViewState(
    val item: RaceItem? = null,
    val sessions: List<Session> = emptyList(),
    val refreshing: Boolean = false
) {
    companion object {
        val Empty = RaceItemDetailsViewState()
    }
}