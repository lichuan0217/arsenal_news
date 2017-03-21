package top.lemonsoda.gunners.utils;

import android.os.Environment;

import java.io.File;

import top.lemonsoda.gunners.BuildConfig;

/**
 * Created by chuanl on 2/24/17.
 */

public class Constants {

    public static final String CACHE_FILE_NAME = "arsenal_news";
    public static final String BASE_URL = "http://101.200.141.146:9000/arsenal/";
    public static final String WEIBO_BASE_URL = "https://api.weibo.com/2/";

    public static final String INTENT_EXTRA_HEADER = "header";
    public static final String INTENT_EXTRA_ARTICLE_ID = "article_id";
    public static final String INTENT_EXTRA_SOURCE = "source_activity";
    public static final String INTENT_EXTRA_IS_FAVORITE = "is_favorite";
    public static final int ID_NEWS_INDEX_ACTIVITY = 0;
    public static final int ID_NEWS_FAVORITE_ACTIVITY = 1;
    public static final int ID_NEWS_RECEIVER = 2;

    public static final String BUGLY_APP_ID = BuildConfig.BUGLY_APP_ID;

    public static final String APP_KEY = BuildConfig.WEIBO_APP_KEY;
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String STORAGE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator
                    + "arsenalnews"
                    + File.separator
                    + "img"
                    + File.separator;
    public static final String AVATAR_NAME = "avatar.png";
    public static final String AVATAR_FILE = STORAGE_PATH + AVATAR_NAME;

    public static final String LOGIN_EVENT_INTENT_ACTION = "top.lemonsoda.arsenalnews.LOGIN_BROADCAST";
    public static final String INTENT_LOGIN_EXTRA_KEY = "intent_login_status";

    public static final String INTENT_EXTRA_USER_ID = "user_id";

    public static final String PREF_KEY_NOTIFY = "key_notify_news";
}
