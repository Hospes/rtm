package ua.hospes.nfs.marathon.ui.teams;

import java.util.List;

import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public interface TeamsContract {
    interface View {
        void updateTeams(List<Team> drivers);
    }
}