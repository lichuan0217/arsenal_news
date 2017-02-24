package top.lemonsoda.gunners.data;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import top.lemonsoda.gunners.data.db.dao.NewsDao;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.data.module.NewsDetail;
import top.lemonsoda.gunners.data.network.NewsService;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsRepository implements NewsDataSource {

    private NewsService newsService;

    private NewsDao newsDao;

    public NewsRepository(NewsService newsService, NewsDao newsDao) {
        this.newsService = newsService;
        this.newsDao = newsDao;
    }

    @Override
    public Observable<List<News>> getNewsFromLocal() {
        Observable<List<News>> observableForGetNewsListFromLocal = Observable.create(
                new Observable.OnSubscribe<List<News>>() {
                    @Override
                    public void call(Subscriber<? super List<News>> subscriber) {
                        try {
                            List<News> newsList = newsDao.queryNews();
                            subscriber.onNext(newsList);
                            subscriber.onCompleted();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return observableForGetNewsListFromLocal;
    }

    @Override
    public Observable<List<News>> getNewsFromNetwork(int page) {
        Observable<List<News>> observableForGetNewsListFromNetwork = newsService.getNewsList(page);

        observableForGetNewsListFromNetwork =
                observableForGetNewsListFromNetwork.doOnNext(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newsList) {
                        try {
                            newsDao.updateNews(newsList);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

        return observableForGetNewsListFromNetwork;
    }

    @Override
    public Observable<NewsDetail> getNewsDetailById(String id) {
        return newsService.getNewsDetail(id);
    }
}
