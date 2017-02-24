package top.lemonsoda.gunners.utils.di;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chuanl on 2/21/17.
 */

@Module
public class ActivityContextModule {

    private final Context context;

    public ActivityContextModule(Activity activity) {
        this.context = activity;
    }

    @ActivityScope
    @Provides
    Context proviceContext() {
        return this.context;
    }
}
