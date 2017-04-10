package ua.hospes.nfs.marathon.data.team.storage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.nfs.marathon.core.db.DbHelper;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.core.db.tables.Teams;
import ua.hospes.nfs.marathon.data.team.mapper.TeamsMapper;
import ua.hospes.nfs.marathon.data.team.models.TeamDb;

/**
 * @author Andrew Khloponin
 */
public class TeamsDbStorage {
    private final DbHelper dbHelper;

    @Inject
    public TeamsDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<TeamDb>> add(TeamDb Team) {
        return dbHelper.insert(Teams.name, Team);
    }

    public Observable<UpdateResult<TeamDb>> update(TeamDb Team) {
        return dbHelper.update(new QueryBuilder(Teams.name).where(Teams._ID + " = ?", String.valueOf(Team.getId())), Team);
    }


    public Observable<Integer> remove(TeamDb Team) {
        return dbHelper.delete(new QueryBuilder(Teams.name).where(Teams._ID + " = ?", String.valueOf(Team.getId())));
    }


    public Observable<TeamDb> getNotInRace() {
        return dbHelper.singleQuery(TeamsMapper::map, "SELECT t1.* FROM " + Teams.name + " t1 LEFT JOIN " + Race.name + " t2 ON t2." + Race.TEAM_ID + " = t1." + Teams._ID + " WHERE t2." + Race.TEAM_ID + " IS NULL");
    }


    public Observable<TeamDb> get() {
        return dbHelper.singleQuery(TeamsMapper::map, new QueryBuilder(Teams.name));
    }

    public Observable<TeamDb> get(int id) {
        return dbHelper.singleQuery(TeamsMapper::map, new QueryBuilder(Teams.name).where(Teams._ID + " = ?", String.valueOf(id)));
    }

    public Observable<List<TeamDb>> listen() {
        return dbHelper.query(TeamsMapper::map, new QueryBuilder(Teams.name));
    }


    public Observable<Void> clear() {
        return dbHelper.delete(new QueryBuilder(Teams.name)).map(integer -> null);
    }
}