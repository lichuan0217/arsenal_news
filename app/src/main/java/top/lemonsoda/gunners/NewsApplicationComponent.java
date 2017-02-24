package top.lemonsoda.gunners;

import android.content.Context;

import dagger.Component;
import top.lemonsoda.gunners.data.NewsRepository;
import top.lemonsoda.gunners.data.NewsRepositoryModule;
import top.lemonsoda.gunners.data.user.UserManager;
import top.lemonsoda.gunners.data.user.UserManagerModule;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

/**
 * Created by chuanl on 2/24/17.
 */

@NewsApplicationScope
@Component(modules = {NewsRepositoryModule.class, UserManagerModule.class})
public interface NewsApplicationComponent {

    NewsRepository getNewsRepository();

    Context getContext();

    UserManager getUserManager();
}
