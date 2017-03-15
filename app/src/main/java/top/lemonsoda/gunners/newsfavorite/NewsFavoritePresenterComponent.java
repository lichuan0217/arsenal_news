package top.lemonsoda.gunners.newsfavorite;

import dagger.Component;
import top.lemonsoda.gunners.NewsApplicationComponent;
import top.lemonsoda.gunners.utils.di.ActivityScope;

/**
 * Created by Chuan on 09/03/2017.
 */

@ActivityScope
@Component(modules = NewsFavoritePresenterModule.class, dependencies = NewsApplicationComponent.class)
public interface NewsFavoritePresenterComponent {

    void inject(NewsFavoriteActivity activity);
}
