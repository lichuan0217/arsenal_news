package top.lemonsoda.gunners.data.network;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.data.module.NewsDetail;
import top.lemonsoda.gunners.data.module.RequestFavorite;
import top.lemonsoda.gunners.data.module.RequestUser;
import top.lemonsoda.gunners.data.module.ResponseFavorite;

/**
 * Created by chuanl on 2/10/17.
 */

public interface NewsService {

    @GET("item/{page}")
    Observable<List<News>> getNewsList(@Path("page") int page);

    @GET("article/{id}")
    Observable<NewsDetail> getNewsDetail(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @POST("article/{id}/")
    Observable<NewsDetail> getArticleWithUserId(@Path("id") String id, @Body RequestUser user);


    @GET("favorites/{id}/")
    Observable<List<News>> getFavorites(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @POST("favorites/")
    Observable<ResponseFavorite> postFavorite(@Body RequestFavorite favorite);

    @DELETE("favorites/{user_id}/{article_id}/")
    Observable<ResponseFavorite> deleteFavorite(
            @Path("user_id") String user_id, @Path("article_id") String article_id);
}
