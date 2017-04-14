package ua.hospes.nfs.marathon.core.di.module;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    PackageManager providePackageManager(Context context) {
        return context.getPackageManager();
    }
}