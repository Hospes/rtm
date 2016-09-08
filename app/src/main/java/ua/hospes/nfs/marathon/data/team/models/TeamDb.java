package ua.hospes.nfs.marathon.data.team.models;

import android.content.ContentValues;

import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;
import ua.hospes.nfs.marathon.core.db.tables.Teams;

/**
 * @author Andrew Khloponin
 */
public class TeamDb implements ModelBaseInterface {
    private int id;
    private String name;


    public TeamDb(int id, String name) {
        this.id = id;
        this.name = name;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    //endregion


    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Teams.NAME, name);

        return cv;
    }
}