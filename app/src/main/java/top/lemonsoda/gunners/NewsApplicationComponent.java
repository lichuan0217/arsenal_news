package top.lemonsoda.gunners;

import android.content.Context;

import dagger.Component;
import top.lemonsoda.gunners.data.NewsRepository;
import top.lemonsoda.gunners.data.NewsRepositoryModule;
import top.lemonsoda.gunners.data.network.WeiboService;
import top.lemonsoda.gunners.data.network.WeiboServiceModule;
import top.lemonsoda.gunners.data.user.AccessTokenKeeper;
import top.lemonsoda.gunners.data.user.BitmapSaver;
import top.lemonsoda.gunners.data.user.UserManager;
import top.lemonsoda.gunners.data.user.UserManagerModule;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

/**
 * Created by chuanl on 2/24/17.
 */

@NewsApplicationScope
@Component(modules = {NewsRepositoryModule.class, UserManagerModule.class, WeiboServiceModule.class})
public interface NewsApplicationComponent {

    NewsRepository getNewsRepository();

    Context getContext();

    UserManager getUserManager();

    AccessTokenKeeper getAccessTokenKeeper();

    WeiboService getWeiboService();

    BitmapSaver getBitmapSaver();
}
