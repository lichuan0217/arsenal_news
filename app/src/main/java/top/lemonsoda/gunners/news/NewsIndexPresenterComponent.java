package top.lemonsoda.gunners.news;

import dagger.Component;
import top.lemonsoda.gunners.NewsApplicationComponent;
import top.lemonsoda.gunners.utils.di.ActivityScope;

/**
 * Created by chuanl on 2/24/17.
 */

@ActivityScope
@Component(modules = {NewsIndexPresenterModule.class}, dependencies = {NewsApplicationComponent.class})
public interface NewsIndexPresenterComponent {

    void inject(NewsIndexActivity activity);
}
