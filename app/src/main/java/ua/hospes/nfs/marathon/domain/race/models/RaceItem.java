package ua.hospes.nfs.marathon.domain.race.models;

import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class RaceItem {
    private final int id;
    private final Team team;
    private int teamNumber = -1;
    private Session session;

    private RaceItemDetails details;


    public RaceItem(Team team) {
        this.id = -1;
        this.team = team;
    }

    public RaceItem(int id, Team team) {
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

    public Session getSession() {
        return session;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public RaceItemDetails getDetails() {
        return details;
    }
    //endregion

    //region Setters
    public void setSession(Session session) {
        this.session = session;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void setDetails(RaceItemDetails details) {
        this.details = details;
    }
    //endregion


    @Override
    public String toString() {
        return "RaceItem{" +
                "id=" + id +
                ", team=" + team +
                ", teamNumber=" + teamNumber +
                ", session=" + session +
                ", details=" + details +
                '}';
    }
}