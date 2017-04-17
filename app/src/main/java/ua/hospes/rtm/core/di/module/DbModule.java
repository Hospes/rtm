package ua.hospes.rtm.core.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ua.hospes.rtm.core.db.DbHelper;

/**
 * @author Andrew Khloponin
 */
@Module
public class DbModule {
    @Provides
    public DbHelper provideDBHelper(Context context) {
        return new DbHelper(context);
    }
}