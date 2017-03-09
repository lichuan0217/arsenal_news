package top.lemonsoda.gunners.data.user;

import android.content.Context;
import android.content.SharedPreferences;

import rx.Observable;
import rx.Subscriber;
import top.lemonsoda.gunners.data.module.User;

/**
 * Created by chuanl on 2/24/17.
 */

public class UserManager {

    private static final String PREFERENCES_NAME = "pref_user";
    private static final String KEY_UID = "uid";
    private static final String KEY_USER_SCREEN_NAME = "screen_name";
    private static final String KEY_USER_AVATAR_URL = "avatar_url";

    private Context context;
    private SharedPreferences sharedPreferences;
    private boolean isUserLogin = false;

    public UserManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);

        isUserLogin = readUserInfo().getId() != 0;
    }

    public boolean isUserLogin() {
        return isUserLogin;
    }

    public void setUserLogin(boolean userLogin) {
        isUserLogin = userLogin;
    }

    /**
     * 保存 User 对象到 SharedPreferences
     */
    public void writeUserInfo(User user) {
        if (null == user) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_UID, user.getId());
        editor.putString(KEY_USER_SCREEN_NAME, user.getScreen_name());
        editor.putString(KEY_USER_AVATAR_URL, user.getProfile_image_url());
        editor.commit();
    }


    /**
     * 从 SharedPreferences 读取 User 信息。
     */
    public User readUserInfo() {
        User user = new User();
        user.setId(sharedPreferences.getLong(KEY_UID, 0));
        user.setScreen_name(sharedPreferences.getString(KEY_USER_SCREEN_NAME, ""));
        user.setProfile_image_url(sharedPreferences.getString(KEY_USER_AVATAR_URL, ""));
        return user;
    }
    
    public Observable<User> getUserInfo() {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                User user = readUserInfo();
                subscriber.onNext(user);
                subscriber.onCompleted();
            }
        });
    }


    /**
     * 清空 SharedPreferences 中 User信息。
     */
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
}
