package top.lemonsoda.gunners.newsfavorite;

import dagger.Component;
import top.lemonsoda.gunners.utils.di.NewsFavoriteFragmentScope;

/**
 * Created by Chuan on 15/03/2017.
 */

@NewsFavoriteFragmentScope
@Component(modules = NewsFavoriteFragmentModule.class)
public interface NewsFavoriteFragmentComponent {

    void inject(NewsFavoriteFragment fragment);
}
