package ua.hospes.rtm.data.team.storage;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.dbhelper.builder.DeleteQuery;
import ua.hospes.dbhelper.builder.SelectQuery;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.DbHelper;
import ua.hospes.rtm.core.db.tables.Drivers;
import ua.hospes.rtm.core.db.tables.Race;
import ua.hospes.rtm.core.db.tables.Teams;
import ua.hospes.rtm.data.team.mapper.TeamsMapper;
import ua.hospes.rtm.data.team.models.TeamDb;

/**
 * @author Andrew Khloponin
 */
public class TeamsDbStorage {
    private final DbHelper dbHelper;

    @Inject
    TeamsDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<TeamDb>> add(TeamDb Team) {
        return dbHelper.insert(Teams.name, SQLiteDatabase.CONFLICT_ABORT, Team);
    }

    public Observable<UpdateResult<TeamDb>> update(TeamDb team) {
        return dbHelper.update(new UpdateQuery(Teams.name).where(Condition.eq(Teams.ID, team.getId())), team);
    }


    public Observable<Integer> remove(TeamDb team) {
        return dbHelper.delete(new DeleteQuery(Teams.name).where(Condition.eq(Teams.ID, team.getId()))).toObservable();
    }


    public Observable<TeamDb> getNotInRace() {
        return dbHelper.querySingle(TeamsMapper::map, "SELECT t1.rowid, t1.* FROM " + Teams.name + " t1 LEFT JOIN " + Race.name + " t2 ON t2." + Race.TEAM_ID + " = t1." + Teams.ID + " WHERE t2." + Race.TEAM_ID + " IS NULL");
    }


    public Observable<TeamDb> get() {
        return dbHelper.querySingle(TeamsMapper::map, new SelectQuery(Teams.name));
    }

    public Observable<TeamDb> get(int id) {
        return dbHelper.querySingle(TeamsMapper::map, new SelectQuery(Teams.name).where(Condition.eq(Teams.ID, id)));
    }

    public Observable<List<TeamDb>> listen() {
        return dbHelper.query(TeamsMapper::map, new SelectQuery(Teams.name), Teams.name, Drivers.name);
    }


    public Observable<Void> clear() {
        return dbHelper.delete(new DeleteQuery(Teams.name)).toObservable().map(integer -> null);
    }
}