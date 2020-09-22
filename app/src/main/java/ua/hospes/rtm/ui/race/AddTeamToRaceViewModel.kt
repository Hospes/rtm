package ua.hospes.rtm.ui.race

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import ua.hospes.rtm.data.RaceRepository
import ua.hospes.rtm.data.SessionsRepository
import ua.hospes.rtm.data.TeamsRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team

class AddTeamToRaceViewModel @ViewModelInject constructor(
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