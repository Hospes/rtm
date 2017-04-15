package ua.hospes.rtm.data.team.models;

import android.content.ContentValues;

import ua.hospes.dbhelper.BaseModelInterface;
import ua.hospes.rtm.core.db.tables.Teams;

/**
 * @author Andrew Khloponin
 */
public class TeamDb implements BaseModelInterface {
    private int id = -1;
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