package top.lemonsoda.gunners.data.network;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;
import top.lemonsoda.gunners.utils.di.RetroiftWeiboQualifier;

import static top.lemonsoda.gunners.utils.Constants.WEIBO_BASE_URL;

/**
 * Created by Chuan on 07/03/2017.
 */

@Module(includes = {NetworkModule.class})
public class WeiboServiceModule {

    @Provides
    @NewsApplicationScope
    @RetroiftWeiboQualifier
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(WEIBO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @NewsApplicationScope
    public WeiboService provideWeiboService(@RetroiftWeiboQualifier Retrofit retrofit) {
        return retrofit.create(WeiboService.class);
    }
}
