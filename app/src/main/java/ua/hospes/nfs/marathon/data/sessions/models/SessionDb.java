package ua.hospes.nfs.marathon.data.sessions.models;

import android.content.ContentValues;

import ua.hospes.dbhelper.BaseModelInterface;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class SessionDb implements BaseModelInterface {
    private int id = -1;
    private final int teamId;
    private int driverId = -1;
    private int carId = -1;
    private long startDurationTime = -1;
    private long endDurationTime = -1;
    private String type;


    public SessionDb(int teamId) {
        this.teamId = teamId;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getDriverId() {
        return driverId;
    }

    public int getCarId() {
        return carId;
    }

    public long getStartDurationTime() {
        return startDurationTime;
    }

    public long getEndDurationTime() {
        return endDurationTime;
    }

    public String getType() {
        return type;
    }
    //endregion

    //region Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setStartDurationTime(long startDurationTime) {
        this.startDurationTime = startDurationTime;
    }

    public void setEndDurationTime(long endDurationTime) {
        this.endDurationTime = endDurationTime;
    }

    public void setType(String type) {
        this.type = type;
    }
    //endregion


    @Override
    public String toString() {
        return "SessionDb{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", driverId=" + driverId +
                ", carId=" + carId +
                ", startDurationTime=" + startDurationTime +
                ", endDurationTime=" + endDurationTime +
                ", type=" + type +
                '}';
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Sessions.TEAM_ID, teamId);
        cv.put(Sessions.DRIVER_ID, driverId);
        cv.put(Sessions.CAR_ID, carId);
        cv.put(Sessions.START_DURATION_TIME, startDurationTime);
        cv.put(Sessions.END_DURATION_TIME, endDurationTime);
        cv.put(Sessions.TYPE, type);

        return cv;
    }
}