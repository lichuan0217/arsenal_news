package top.lemonsoda.gunners.newsdetail;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chuanl on 2/24/17.
 */

@Module
public class NewsDetailPresenterModule {

    private final NewsDetailContract.View view;
    private final String id;

    public NewsDetailPresenterModule(NewsDetailContract.View view, String id) {
        this.view = view;
        this.id = id;
    }

    @Provides
    NewsDetailContract.View provideNewsIndexContractView() {
        return view;
    }

    @Provides
    String provideArticleId() {
        return id;
    }
}
