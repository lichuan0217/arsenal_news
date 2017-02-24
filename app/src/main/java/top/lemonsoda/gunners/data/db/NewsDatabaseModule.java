package top.lemonsoda.gunners.data.db;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import top.lemonsoda.gunners.NewsApplicationModule;
import top.lemonsoda.gunners.data.db.dao.NewsDao;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

/**
 * Created by chuanl on 2/24/17.
 */

@Module(includes = NewsApplicationModule.class)
public class NewsDatabaseModule {

    @Provides
    @NewsApplicationScope
    public NewsDao provideNewsDao(Context context) {
        return new NewsDao(context);
    }
}
