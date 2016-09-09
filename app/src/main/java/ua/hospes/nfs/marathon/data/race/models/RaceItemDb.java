package ua.hospes.nfs.marathon.data.race.models;

import android.content.ContentValues;

import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;
import ua.hospes.nfs.marathon.core.db.tables.Race;

/**
 * @author Andrew Khloponin
 */
public class RaceItemDb implements ModelBaseInterface {
    private int id;
    private final int teamId;
    private int order;


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

    public int getOrder() {
        return order;
    }
    //endregion

    //region Setters
    public void setId(int id) {
        this.id = id;
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
                '}';
    }


    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Race.TEAM_ID, teamId);

        return cv;
    }
}