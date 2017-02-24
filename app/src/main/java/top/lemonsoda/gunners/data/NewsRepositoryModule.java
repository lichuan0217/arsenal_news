package top.lemonsoda.gunners.data;

import dagger.Module;
import dagger.Provides;
import top.lemonsoda.gunners.data.db.NewsDatabaseModule;
import top.lemonsoda.gunners.data.db.dao.NewsDao;
import top.lemonsoda.gunners.data.network.NewsService;
import top.lemonsoda.gunners.data.network.NewsServiceModule;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

/**
 * Created by chuanl on 2/24/17.
 */

@Module(includes = {NewsServiceModule.class, NewsDatabaseModule.class})
public class NewsRepositoryModule {

    @Provides
    @NewsApplicationScope
    NewsRepository provideNewsRepository(NewsService newsService, NewsDao newsDao) {
        return new NewsRepository(newsService, newsDao);
    }
}
