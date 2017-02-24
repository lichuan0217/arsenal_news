package top.lemonsoda.gunners.data.network;

import android.content.Context;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;
import top.lemonsoda.gunners.NewsApplicationModule;
import top.lemonsoda.gunners.utils.Constants;
import top.lemonsoda.gunners.utils.di.NewsApplicationScope;

/**
 * Created by chuanl on 2/15/17.
 */

@Module(includes = NewsApplicationModule.class)
public class NetworkModule {

    @Provides
    @NewsApplicationScope
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Timber.i(message);
                    }
                }
        );
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @NewsApplicationScope
    public Cache provideCache(Context context) {
        File cacheFile = new File(context.getCacheDir(), Constants.CACHE_FILE_NAME);
        cacheFile.mkdirs();
        Cache cache = new Cache(cacheFile, 10 * 1000 * 1000); //10MB
        return cache;
    }


    @Provides
    @NewsApplicationScope
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
    }


}
