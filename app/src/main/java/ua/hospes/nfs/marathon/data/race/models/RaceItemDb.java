package ua.hospes.nfs.marathon.data.race.models;

import android.content.ContentValues;

import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;
import ua.hospes.nfs.marathon.core.db.tables.Race;

/**
 * @author Andrew Khloponin
 */
public class RaceItemDb implements ModelBaseInterface {
    private int id = -1;
    private final int teamId;
    private int teamNumber = -1;
    private int sessionId = -1;
    private int order = 0;


    public RaceItemDb(int teamId) {
        this.teamId = teamId;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public int getSessionId() {
        return sessionId;
    }

    public int getOrder() {
        return order;
    }
    //endregion

    //region Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    //endregion


    @Override
    public String toString() {
        return "RaceItemDb{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", teamNumber=" + teamNumber +
                ", sessionId=" + sessionId +
                ", order=" + order +
                '}';
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Race.TEAM_ID, teamId);
        cv.put(Race.TEAM_NUMBER, teamNumber);
        cv.put(Race.SESSION_ID, sessionId);

        return cv;
    }
}