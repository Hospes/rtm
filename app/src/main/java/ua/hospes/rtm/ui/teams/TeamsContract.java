package ua.hospes.rtm.ui.teams;

import java.util.List;

import ua.hospes.rtm.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
interface TeamsContract {
    interface View {
        void updateTeams(List<Team> drivers);
    }
}