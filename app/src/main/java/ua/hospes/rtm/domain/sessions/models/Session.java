package ua.hospes.rtm.domain.sessions.models;

import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
public class Session {
    private final int id;
    private final int teamId;
    private Driver driver = null;
    private Car car = null;
    private long raceStartTime = -1;
    private long startDurationTime = -1;
    private long endDurationTime = -1;
    private Type type = Type.TRACK;


    public Session(int teamId) {
        this.id = -1;
        this.teamId = teamId;
    }

    public Session(int id, int teamId) {
        this.id = id;
        this.teamId = teamId;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public int getTeamId() {
        return teamId;
    }

    public Driver getDriver() {
        return driver;
    }

    public Car getCar() {
        return car;
    }

    public long getRaceStartTime() {
        return raceStartTime;
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

    public void setCar(Car car) {
        this.car = car;
    }

    public void setRaceStartTime(long raceStartTime) {
        this.raceStartTime = raceStartTime;
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
                ", teamId=" + teamId +
                ", driver=" + driver +
                ", car=" + car +
                ", raceStartTime=" + raceStartTime +
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