package top.lemonsoda.gunners.data.network;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.data.module.NewsDetail;

/**
 * Created by chuanl on 2/10/17.
 */

public interface NewsService {

    @GET("item/{page}")
    Observable<List<News>> getNewsList(@Path("page") int page);

    @GET("article/{id}")
    Observable<NewsDetail> getNewsDetail(@Path("id") String id);
}
