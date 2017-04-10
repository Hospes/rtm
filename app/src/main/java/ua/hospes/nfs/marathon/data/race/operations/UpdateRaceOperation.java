package ua.hospes.nfs.marathon.data.race.operations;

import android.content.ContentValues;
import android.util.Pair;

import com.squareup.sqlbrite.BriteDatabase;

import ua.hospes.dbhelper.Operation;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.data.race.models.RaceItemDb;

/**
 * @author Andrew Khloponin
 */
public class UpdateRaceOperation implements Operation<Boolean> {
    private final QueryBuilder builder;
    private final ContentValues cv;

    public UpdateRaceOperation(RaceItemDb item) {
        this.cv = item.toContentValues();
        this.builder = new QueryBuilder(Race.name).where(Race._ID + " = ?", String.valueOf(item.getId()));
    }

    public UpdateRaceOperation(Pair<Integer, ContentValues> pair) {
        this.cv = pair.second;
        this.builder = new QueryBuilder(Race.name).where(Race.TEAM_ID + " = ?", String.valueOf(pair.first));
    }

    @Override
    public Boolean doOperation(BriteDatabase db) {
        return db.update(builder.getTable(), cv, builder.getSelection(), builder.getSelectionArgs()) > 0;
    }
}