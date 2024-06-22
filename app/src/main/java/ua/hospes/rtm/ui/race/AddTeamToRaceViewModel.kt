package ua.hospes.rtm.ui.race

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.hospes.rtm.data.model.SessionDto
import ua.hospes.rtm.data.model.TeamDto
import ua.hospes.rtm.data.repo.RaceRepository
import ua.hospes.rtm.data.repo.SessionsRepository
import ua.hospes.rtm.data.repo.TeamsRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.race.models.toDto
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.toDomain
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.toDomain
import javax.inject.Inject

@HiltViewModel
class AddTeamToRaceViewModel @Inject constructor(
    private val sessionsRepo: SessionsRepository,
    private val teamsRepo: TeamsRepository,
    private val raceRepo: RaceRepository
) : ViewModel() {

    suspend fun getTeams() = teamsRepo.getNotInRace().map(TeamDto::toDomain)

    suspend fun save(number: String, team: Team? = null) {
        val intNumber = number.toInt()

        team ?: throw IllegalArgumentException("Team not selected")
        val session = sessionsRepo.newSession(SessionDto.Type.TRACK, team.id)

        raceRepo.save(RaceItem(teamNumber = intNumber, team = team, session = session.toDomain()).toDto())
    }
}