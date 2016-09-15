package ua.hospes.nfs.marathon.core.db;

import com.squareup.sqlbrite.BriteDatabase;

/**
 * @author Andrew Khloponin
 */
public interface Operation<RESULT> {
    RESULT doOperation(BriteDatabase db);
}