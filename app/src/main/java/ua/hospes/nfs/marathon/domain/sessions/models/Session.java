package ua.hospes.nfs.marathon.domain.sessions.models;

import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class Session {
    private final int id;
    private final Team team;
    private Driver driver = null;
    private long startDurationTime = -1;
    private long endDurationTime = -1;
    private Type type = Type.TRACK;


    public Session(Team team) {
        this.id = -1;
        this.team = team;
    }

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

    public Driver getDriver() {
        return driver;
    }

    public long getStartDurationTime() {
        return startDurationTime;
    }

    public long getEndDurationTime() {
        return endDurationTime;
    }

    public Type getType() {
        return type == null ? type = Type.TRACK : type;
    }
    //endregion

    //region Setters
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setStartDurationTime(long startDurationTime) {
        this.startDurationTime = startDurationTime;
    }

    public void setEndDurationTime(long endDurationTime) {
        this.endDurationTime = endDurationTime;
    }

    public void setType(Type type) {
        this.type = type == null ? Type.TRACK : type;
    }

    public void setType(String type) {
        try {
            this.type = Type.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            this.type = Type.TRACK;
        }
    }
    //endregion


    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", team=" + team +
                ", driver=" + driver +
                ", startDurationTime=" + startDurationTime +
                ", endDurationTime=" + endDurationTime +
                ", type=" + type +
                '}';
    }


    public enum Type {
        TRACK("ON TRACK"),
        PIT("PIT-STOP");

        private String title;

        Type(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}