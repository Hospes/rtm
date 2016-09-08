package ua.hospes.nfs.marathon.core.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Andrew Khloponin
 */
@Module
public class DataModule {
    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder().create();
    }
}