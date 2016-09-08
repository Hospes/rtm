package ua.hospes.nfs.marathon.core.di.module;

import android.app.Activity;

import dagger.Module;

/**
 * @author Andrew Khloponin
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

//    @Provides
//    @ActivityScope
//    @AutoExpose(BaseActivity.class)
//    public Activity provideActivity() {
//        return mActivity;
//    }

//    @Provides
//    @ActivityScope
//    @AutoExpose(BaseActivity.class)
//    public ErrorHandler provideErrorHandler(DefaultErrorHandler errorHandler) {
//        return errorHandler;
//    }
}