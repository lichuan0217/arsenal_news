package top.lemonsoda.gunners.data.network;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

import static top.lemonsoda.gunners.utils.Constants.BASE_URL;

/**
 * Created by chuanl on 2/15/17.
 */

@Module(includes = {NetworkModule.class})
public class NewsServiceModule {

    @Provides
    @NewsApplicationScope
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @NewsApplicationScope
    public NewsService provideNewsService(Retrofit retrofit) {
        return retrofit.create(NewsService.class);
    }
}
