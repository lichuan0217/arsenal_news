package top.lemonsoda.gunners;

import android.app.Activity;
import android.app.Application;

import cn.jpush.android.api.JPushInterface;
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

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        component = DaggerNewsApplicationComponent.builder()
                .newsApplicationModule(new NewsApplicationModule(this))
                .build();

    }

    public NewsApplicationComponent getComponent() {
        return component;
    }
}
