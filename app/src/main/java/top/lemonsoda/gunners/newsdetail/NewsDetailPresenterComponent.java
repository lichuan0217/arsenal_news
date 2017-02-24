package top.lemonsoda.gunners.newsdetail;

import dagger.Component;
import top.lemonsoda.gunners.NewsApplicationComponent;
import top.lemonsoda.gunners.utils.di.ActivityScope;

/**
 * Created by chuanl on 2/24/17.
 */

@ActivityScope
@Component(modules = NewsDetailPresenterModule.class, dependencies = {NewsApplicationComponent.class})
public interface NewsDetailPresenterComponent {

    void inject(NewsDetailActivity activity);
}

