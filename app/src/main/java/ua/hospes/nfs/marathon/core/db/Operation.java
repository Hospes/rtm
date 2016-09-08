package ua.hospes.nfs.marathon.core.db;

import com.squareup.sqlbrite.BriteDatabase;

/**
 * @author Andrew Khloponin
 */
public interface Operation {
    long doOperation(BriteDatabase db);
}