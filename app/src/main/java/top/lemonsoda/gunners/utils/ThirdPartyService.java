package top.lemonsoda.gunners.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Chuan on 21/03/2017.
 */

public class ThirdPartyService {

    private static Context context;

    public static void init(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public static void initJPush() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotifyEnable = sharedPreferences.getBoolean(Constants.PREF_KEY_NOTIFY, true);
        if (isNotifyEnable) {
            JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
            JPushInterface.init(context);         // 初始化 JPush
        }
    }

    public static void initBugly() {
        String packageName = context.getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(context, Constants.BUGLY_APP_ID, true, strategy);

    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
