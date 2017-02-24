package top.lemonsoda.gunners;

import android.app.Activity;
import android.app.Application;

import timber.log.Timber;

/**
 * Created by chuanl on 2/24/17.
 */

public class NewsApplication extends Application {

    public static NewsApplication get(Activity activity) {
        return (NewsApplication) activity.getApplication();
    }

    NewsApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        component = DaggerNewsApplicationComponent.builder()
                .newsApplicationModule(new NewsApplicationModule(this))
                .build();

    }

    public NewsApplicationComponent getComponent() {
        return component;
    }
}
