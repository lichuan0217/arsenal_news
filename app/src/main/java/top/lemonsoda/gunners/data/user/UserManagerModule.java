package top.lemonsoda.gunners.data.user;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import top.lemonsoda.gunners.NewsApplicationModule;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

/**
 * Created by chuanl on 2/24/17.
 */

@Module(includes = NewsApplicationModule.class)
public class UserManagerModule {

    @Provides
    @NewsApplicationScope
    UserManager provideUserManager(Context context) {
        return new UserManager(context);
    }

    @Provides
    @NewsApplicationScope
    AccessTokenKeeper provideAccessTokenKeeper(Context context) {
        return new AccessTokenKeeper(context);
    }

    @Provides
    @NewsApplicationScope
    BitmapSaver provideBitmapSaver(Context context) {
        return BitmapSaver.getInstance(context);
    }
}
