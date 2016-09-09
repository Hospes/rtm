package ua.hospes.nfs.marathon.data.drivers.models;

import android.content.ContentValues;

import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;
import ua.hospes.nfs.marathon.core.db.tables.Drivers;

/**
 * @author Andrew Khloponin
 */
public class DriverDb implements ModelBaseInterface {
    private final int id;
    private String name;
    private int teamId;


    public DriverDb(int id, String name, int teamId) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
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


    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Drivers.NAME, name);
        cv.put(Drivers.TEAM_ID, teamId);

        return cv;
    }
}