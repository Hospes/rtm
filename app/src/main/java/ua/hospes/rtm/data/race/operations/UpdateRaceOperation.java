package ua.hospes.rtm.data.race.operations;

import android.content.ContentValues;
import android.util.Pair;

import com.squareup.sqlbrite2.BriteDatabase;

import ua.hospes.dbhelper.Operation;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.tables.Race;
import ua.hospes.rtm.data.race.models.RaceItemDb;

/**
 * @author Andrew Khloponin
 */
public class UpdateRaceOperation implements Operation<Boolean> {
    private final UpdateQuery query;
    private final ContentValues cv;

    public UpdateRaceOperation(RaceItemDb item) {
        this.cv = item.toContentValues();
        this.query = new UpdateQuery(Race.name).where(Condition.eq(Race.ID, item.getId()));
    }

    public UpdateRaceOperation(Pair<Integer, ContentValues> pair) {
        this.cv = pair.second;
        this.query = new UpdateQuery(Race.name).where(Condition.eq(Race.ID, pair.first));
    }

    @Override
    public Boolean doOperation(BriteDatabase db) {
        return db.update(query.getTable(), cv, query.getWhereClause(), query.getWhereArgs()) > 0;
    }
}