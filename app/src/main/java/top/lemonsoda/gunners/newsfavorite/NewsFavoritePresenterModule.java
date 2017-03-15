package top.lemonsoda.gunners.newsfavorite;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chuan on 09/03/2017.
 */
@Module
public class NewsFavoritePresenterModule {
    private final NewsFavoriteContract.View view;

    public NewsFavoritePresenterModule(NewsFavoriteContract.View view) {
        this.view = view;
    }

    @Provides
    NewsFavoriteContract.View provideNewsFavoriteContractView() {
        return view;
    }
}
