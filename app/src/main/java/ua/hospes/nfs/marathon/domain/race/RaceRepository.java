package ua.hospes.nfs.marathon.domain.race;

import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;

/**
 * @author Andrew Khloponin
 */
public interface RaceRepository {
    Observable<RaceItem> get();

    Observable<List<RaceItem>> listen();

    Observable<Boolean> addNew(RaceItem... items);

    Observable<Boolean> delete(RaceItem item);
}