package ua.hospes.rtm.ui.race

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.hospes.rtm.data.RaceRepository
import ua.hospes.rtm.data.SessionsRepository
import ua.hospes.rtm.data.TeamsRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team
import javax.inject.Inject

@HiltViewModel
class AddTeamToRaceViewModel @Inject constructor(
    private val sessionsRepo: SessionsRepository,
    private val teamsRepo: TeamsRepository,
    private val raceRepo: RaceRepository
) : ViewModel() {

    suspend fun getTeams() = teamsRepo.getNotInRace()

    suspend fun save(number: String, team: Team? = null) {
        val intNumber = number.toInt()

        team ?: throw IllegalArgumentException("Team not selected")
        val session = sessionsRepo.newSession(Session.Type.TRACK, team.id)

        raceRepo.save(RaceItem(teamNumber = intNumber, team = team, session = session))
    }
}