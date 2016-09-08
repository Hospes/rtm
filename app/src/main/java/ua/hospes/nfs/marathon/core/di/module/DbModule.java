package ua.hospes.nfs.marathon.core.di.module;

import android.content.Context;

import javax.inject.Singleton;

import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;
import ua.hospes.nfs.marathon.App;
import ua.hospes.nfs.marathon.core.db.DbHelper;

/**
 * @author Andrew Khloponin
 */
@Module
public class DbModule {
    @Singleton
    @Provides
    @AutoExpose(App.class)
    public DbHelper provideDBHelper(Context context) {
        return new DbHelper(context);
    }
}