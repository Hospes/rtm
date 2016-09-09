package ua.hospes.nfs.marathon.domain.sessions.models;

import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class Session {
    private final int id;
    private final Team team;


    public Session(int id, Team team) {
        this.id = id;
        this.team = team;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }
    //endregion
}