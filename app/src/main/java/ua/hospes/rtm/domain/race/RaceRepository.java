package ua.hospes.rtm.domain.race;

import android.content.ContentValues;
import android.util.Pair;

import java.util.List;

import rx.Observable;
import ua.hospes.rtm.domain.race.models.RaceItem;

/**
 * @author Andrew Khloponin
 */
public interface RaceRepository {
    Observable<RaceItem> get();

    Observable<List<RaceItem>> listen();

    Observable<Boolean> addNew(RaceItem... items);

    Observable<Boolean> update(List<RaceItem> items);

    Observable<Boolean> updateByTeamId(Iterable<Pair<Integer, ContentValues>> items);

    Observable<Boolean> delete(RaceItem item);

    Observable<Void> reset();

    Observable<Void> clean();
}