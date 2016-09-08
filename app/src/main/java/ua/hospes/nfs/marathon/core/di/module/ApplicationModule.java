package ua.hospes.nfs.marathon.core.di.module;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import javax.inject.Singleton;

import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;
import ua.hospes.nfs.marathon.App;

@Module
@Singleton
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    @AutoExpose(App.class)
    public Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    @AutoExpose(App.class)
    public PackageManager providePackageManager(Context context) {
        return context.getPackageManager();
    }
}