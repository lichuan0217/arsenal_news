package top.lemonsoda.gunners.data;

import java.util.List;

import rx.Observable;
import top.lemonsoda.gunners.data.module.News;
import top.lemonsoda.gunners.data.module.NewsDetail;
import top.lemonsoda.gunners.data.module.RequestFavorite;
import top.lemonsoda.gunners.data.module.RequestUser;
import top.lemonsoda.gunners.data.module.ResponseFavorite;

/**
 * Created by chuanl on 2/10/17.
 */

public interface NewsDataSource {

    Observable<List<News>> getNewsFromLocal();

    Observable<List<News>> getNewsFromNetwork(int page);

    Observable<NewsDetail> getNewsDetailById(String id);

    Observable<NewsDetail> getNewsDetailByIdWithUserId(String id, RequestUser user);

    Observable<List<News>> getFavorites(String id);

    Observable<ResponseFavorite> postFavorite(RequestFavorite favorite);

    Observable<ResponseFavorite> deleteFavorite(String articleId, String userId);
}
