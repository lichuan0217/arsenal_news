package top.lemonsoda.gunners.data.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Chuan on 02/03/2017.
 */

public class AccessTokenKeeper {

    private static final String PREFERENCES_NAME = "pref_weibo";

    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";

    private Context context;
    private SharedPreferences sharedPreferences;

    public AccessTokenKeeper(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
    }

    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param token Token 对象
     */
    public void writeAccessToken(Oauth2AccessToken token) {
        if (null == token) {
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_UID, token.getUid());
        editor.putString(KEY_ACCESS_TOKEN, token.getToken());
        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        editor.commit();
    }


    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @return 返回 Token 对象
     */
    public Oauth2AccessToken readAccessToken() {

        Oauth2AccessToken token = new Oauth2AccessToken();
        token.setUid(sharedPreferences.getString(KEY_UID, ""));
        token.setToken(sharedPreferences.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(sharedPreferences.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(sharedPreferences.getLong(KEY_EXPIRES_IN, 0));

        return token;
    }


    /**
     * 清空 SharedPreferences 中 Token信息。
     */
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
