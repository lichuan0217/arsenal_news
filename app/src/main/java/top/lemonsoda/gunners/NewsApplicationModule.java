package top.lemonsoda.gunners;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

/**
 * Created by chuanl on 2/15/17.
 */

@Module
public class NewsApplicationModule {

    private final Context context;

    public NewsApplicationModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @NewsApplicationScope
    public Context provideContext() {
        return context;
    }
}
