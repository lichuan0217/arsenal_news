package top.lemonsoda.gunners.data;

import java.util.List;

import rx.Observable;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.data.module.NewsDetail;

/**
 * Created by chuanl on 2/10/17.
 */

public interface NewsDataSource {

    Observable<List<News>> getNewsFromLocal();

    Observable<List<News>> getNewsFromNetwork(int page);

    Observable<NewsDetail> getNewsDetailById(String id);

    Observable<List<News>> getFavorites(String id);

}
