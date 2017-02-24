package top.lemonsoda.gunners.news;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chuanl on 2/24/17.
 */

@Module
public class NewsIndexPresenterModule {

    private final NewsIndexContract.View view;

    public NewsIndexPresenterModule(NewsIndexContract.View view) {
        this.view = view;
    }

    @Provides
    NewsIndexContract.View provideNewsIndexContractView() {
        return view;
    }
}
