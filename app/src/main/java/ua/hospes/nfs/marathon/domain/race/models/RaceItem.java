package ua.hospes.nfs.marathon.domain.race.models;

import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class RaceItem {
    private final int id;
    private final Team team;


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
    //endregion


    @Override
    public String toString() {
        return "RaceItem{" +
                "id=" + id +
                ", team=" + team +
                '}';
    }
}