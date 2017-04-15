package ua.hospes.rtm.data.drivers.models;

import android.content.ContentValues;

import ua.hospes.dbhelper.BaseModelInterface;
import ua.hospes.rtm.core.db.tables.Drivers;

/**
 * @author Andrew Khloponin
 */
public class DriverDb implements BaseModelInterface {
    private final int id;
    private String name;
    private int teamId = -1;


    public DriverDb() {
        this.id = -1;
    }

    public DriverDb(int id) {
        this.id = id;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTeamId() {
        return teamId;
    }
    //endregion

    //region Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    //endregion


    @Override
    public String toString() {
        return "DriverDb{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teamId=" + teamId +
                '}';
    }


    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Drivers.NAME, name);
        cv.put(Drivers.TEAM_ID, teamId);

        return cv;
    }
}