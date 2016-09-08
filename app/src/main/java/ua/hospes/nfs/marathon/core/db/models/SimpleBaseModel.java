package ua.hospes.nfs.marathon.core.db.models;

import android.content.ContentValues;

import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;


public class SimpleBaseModel implements ModelBaseInterface {
    private ContentValues cv;

    public SimpleBaseModel(ContentValues cv) {
        this.cv = cv;
    }

    @Override
    public ContentValues toContentValues() {
        return cv;
    }
}