package top.lemonsoda.gunners.news;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;
import top.lemonsoda.gunners.utils.di.NewsIndexFragmentScope;

/**
 * Created by chuanl on 2/24/17.
 */

@Module
public class NewsIndexFragmentModule {

    private final Context context;

    public NewsIndexFragmentModule(Context context) {
        this.context = context;
    }

    @Provides
    @NewsIndexFragmentScope
    Context provideContext() {
        return context;
    }

    @Provides
    @NewsIndexFragmentScope
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }
}