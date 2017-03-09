package top.lemonsoda.gunners.data.network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import top.lemonsoda.gunners.data.module.User;

/**
 * Created by Chuan on 07/03/2017.
 */

public interface WeiboService {

    @GET("users/show.json")
    Observable<User> getUser(@Query("uid") long uid, @Query("access_token") String token);
}
