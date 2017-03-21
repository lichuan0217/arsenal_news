package top.lemonsoda.gunners;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;
import top.lemonsoda.gunners.utils.Constants;
import top.lemonsoda.gunners.utils.ThirdPartyService;

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

        initThirdPartyService();

        component = DaggerNewsApplicationComponent.builder()
                .newsApplicationModule(new NewsApplicationModule(this))
                .build();

    }

    public NewsApplicationComponent getComponent() {
        return component;
    }

    private void initThirdPartyService() {
        ThirdPartyService.init(this);
        ThirdPartyService.initJPush();
        ThirdPartyService.initBugly();
    }
}
