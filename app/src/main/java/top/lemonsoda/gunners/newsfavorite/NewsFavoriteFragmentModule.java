package top.lemonsoda.gunners.newsfavorite;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;
import top.lemonsoda.gunners.utils.di.NewsFavoriteFragmentScope;

/**
 * Created by Chuan on 15/03/2017.
 */

@Module
public class NewsFavoriteFragmentModule {

    private final Context context;

    public NewsFavoriteFragmentModule(Context context) {
        this.context = context;
    }

    @Provides
    @NewsFavoriteFragmentScope
    Context provideContext() {
        return context;
    }

    @Provides
    @NewsFavoriteFragmentScope
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }
}
